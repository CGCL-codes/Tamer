package com.seavenois.tetris;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class Init extends Activity {

    private Button btnNewGame, btnResumeGame, btnHighScores;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.init);
        btnNewGame = (Button) findViewById(R.id.buttonNewGame);
        btnNewGame.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Game"));
                startActivity(intent);
            }
        });
        btnResumeGame = (Button) findViewById(R.id.buttonResumeGame);
        btnResumeGame.setEnabled(false);
        btnResumeGame.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Game"));
                startActivity(intent);
            }
        });
        btnHighScores = (Button) findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.HighScores"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.initmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        intent = new Intent();
        switch(item.getItemId()) {
            case R.id.menuItemChangelog:
                intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Changelog"));
                break;
            case R.id.menuItemPreferences:
                intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Preferences"));
                break;
            case R.id.menuItemDonate:
                intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Donate"));
                break;
        }
        startActivity(intent);
        return true;
    }
}
