package com.googlecode.toolkits;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.googlecode.toolkits.stardict.StarDict;

public class YAStarDictMain extends Activity {

    private EditText et;

    private TextView tv;

    private StarDict stardict;

    private Button button;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        stardict = new StarDict();
        tv = (TextView) this.findViewById(R.id.TextViewExplaination);
        et = (EditText) this.findViewById(R.id.EditTextWord);
        button = (Button) this.findViewById(R.id.ButtonClear);
        et.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                tv.setText(et.getText().toString());
                String word = et.getText().toString();
                String exp = stardict.getExplanation(word);
                tv.setText(exp);
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv.setText("Please input a word");
                et.setText("");
            }
        });
    }
}
