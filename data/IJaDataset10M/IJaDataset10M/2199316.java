package com.vnpgame.undersea.lostgame.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.vnpgame.chickenmerrychristmas.R;
import com.vnpgame.undersea.database.DBAdapter;

public class LostGameScreen extends Activity implements OnClickListener {

    int level = 0;

    int score = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lostgame);
        level = getIntent().getIntExtra("arg0", 0);
        score = getIntent().getIntExtra("arg1", 0);
        TextView tVLevel = (TextView) findViewById(R.id.TextView02);
        tVLevel.setText("Level : " + level);
        TextView tVScore = (TextView) findViewById(R.id.TextView01);
        tVScore.setText("Score : " + score);
        findViewById(R.id.Button01).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
    }

    public void onClick(View v) {
        if (R.id.button1 == v.getId()) {
            setResult(RESULT_OK);
            finish();
        } else {
            String name = ((EditText) findViewById(R.id.EditText1)).getText().toString();
            DBAdapter adapter = new DBAdapter(this);
            adapter.saveScore(0, name, "" + score, "" + level);
            setResult(RESULT_OK);
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
