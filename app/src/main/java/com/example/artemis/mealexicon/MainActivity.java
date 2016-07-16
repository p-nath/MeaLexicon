package com.example.artemis.mealexicon;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private EditText etWord;
    private TextView tvMeaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);

        database = openOrCreateDatabase("dictionary", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String sql = "create table if not exists dictionary (wid integer primary key autoincrement, " +
                "word text not null, meaning text not null)";
        database.execSQL(sql);

        etWord = (EditText)findViewById(R.id.etWord);
        tvMeaning = (TextView)findViewById(R.id.tvMeaning);
    }

    public void onSearch(View view) {
        final String word = etWord.getText().toString();
        tvMeaning.setText("");
        String sql = "select meaning from dictionary where word = '" + word + "'";
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.getCount() > 0)
            while (cursor.moveToNext()) ///returns True
                tvMeaning.append(cursor.getString(0) + "; ");
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Add word")
                    .setMessage("Do you want to add "+word+" to the dictionary?");

            final EditText meaning = new EditText(this);


            dialog.setView(meaning);

            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String sql = String.format("insert into dictionary (word, meaning) values ('%s', '%s')", word, meaning.getText().toString());
                            database.execSQL(sql);
                        }
                    })
                    .show();
        }
    }

    public void Add_Def(View view) {
        final String word = etWord.getText().toString();
        AlertDialog.Builder def_adder = new AlertDialog.Builder(this);
        final EditText definition = new EditText(this);

        if (word.equals("")) Toast.makeText(MainActivity.this, "Invalid Entry", Toast.LENGTH_SHORT).show();
        else {
            def_adder.setTitle("Add Definition")
                    .setMessage("Enter :")
                    .setView(definition)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface def_adder, int which) {
                            def_adder.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface def_adder, int which) {
                            String sql = String.format("insert into dictionary (word, meaning) values ('%s', '%s')", word, definition.getText().toString());
                            database.execSQL(sql);
                        }
                    })
                    .show();
        }
    }
}

