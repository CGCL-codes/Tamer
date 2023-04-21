package com.sf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sf.util.AbsSubActivity;

public class A3Activity extends AbsSubActivity {

    private TextView tv;

    private EditText et;

    private Button btn1, btn2, btn3, btn4, btn5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = ((TextView) findViewById(R.id.test));
        et = (EditText) findViewById(R.id.et);
        btn1 = ((Button) findViewById(R.id.btn));
        btn2 = ((Button) findViewById(R.id.btn2));
        btn3 = ((Button) findViewById(R.id.btn3));
        btn4 = ((Button) findViewById(R.id.btn4));
        btn5 = ((Button) findViewById(R.id.btn5));
        tv.setText("A3Activity");
        btn1.setText("�޲���");
        btn2.setText("�޲���");
        btn3.setText("���Group��OutActivity(startActivity��ʽ)");
        btn4.setText("���Group��OutActivity(ForResult��ʽ)");
        btn1.setOnClickListener(myClickListener);
        btn2.setOnClickListener(myClickListener);
        btn3.setOnClickListener(myClickListener);
        btn4.setOnClickListener(myClickListener);
        btn5.setOnClickListener(myClickListener);
    }

    private OnClickListener myClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == findViewById(R.id.btn)) {
            } else if (v == findViewById(R.id.btn2)) {
            } else if (v == findViewById(R.id.btn3)) {
                startActivity(new Intent(A3Activity.this, OutActivity.class));
            } else if (v == findViewById(R.id.btn4)) {
                startActivityForResult(new Intent(A3Activity.this, OutActivity.class), 2);
            } else if (v == findViewById(R.id.btn5)) {
                gobackWithResult(3, getIntent());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == 2) {
            et.setText("��OutActivity������   " + data.getStringExtra("data"));
        }
    }
}
