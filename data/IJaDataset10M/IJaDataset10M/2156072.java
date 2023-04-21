package com.show.omokgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

public class OmokGame extends Activity implements OnClickListener {

    public static final int BLACK = 0;

    public static final int WHITE = 1;

    public static final int NOSTONE = 3;

    public static final int HUMAN = 0;

    public static final int AI = 1;

    public static final int boardSizeX = 15;

    public static final int boardSizeY = 15;

    private static final String TAG = "Gomoku";

    public int diffLevel = 0;

    public boolean isSoundOn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.gamestart_button);
        newButton.setOnClickListener(this);
        View optionButton = findViewById(R.id.options_button);
        optionButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.about_button:
                Intent i = new Intent(OmokGame.this, about.class);
                startActivity(i);
                break;
            case R.id.gamestart_button:
                openNewGameDialog();
                break;
            case R.id.options_button:
                Intent h = new Intent(this, Options.class);
                startActivity(h);
                break;
            case R.id.exit_button:
                finish();
                break;
            case R.id.continue_button:
                Toast.makeText(this, "Currently, Not Available", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void openNewGameDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.new_game_title).setItems(R.array.difficulty, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i) {
                diffLevel = i;
                startGame();
            }
        }).show();
    }

    private void startGame() {
        Intent g = new Intent(this, Game.class);
        startActivity(g);
    }
}
