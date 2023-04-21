package com.android.wb.activity;

import com.android.wb.activity.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WordBoxMenu extends WordBoxActivity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Button ButtonTutorial;
        Button ButtonScores;
        Button ButtonGame;
        Button ButtonExit;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordbox_menu);
        ButtonTutorial = (Button) findViewById(R.id.ButtonTutorialID);
        ButtonTutorial.setOnClickListener(this);
        ButtonTutorial = (Button) findViewById(R.id.ButtonTutorialID);
        ButtonTutorial.setOnClickListener(this);
        ButtonScores = (Button) findViewById(R.id.ButtonScoresID);
        ButtonScores.setOnClickListener(this);
        ButtonExit = (Button) findViewById(R.id.ButtonExitID);
        ButtonExit.setOnClickListener(this);
        ButtonScores = (Button) findViewById(R.id.ButtonScoresID);
        ButtonScores.setOnClickListener(this);
        ButtonGame = (Button) findViewById(R.id.ButtonGameID);
        ButtonGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View src) {
        switch(src.getId()) {
            case R.id.ButtonTutorialID:
                Intent tutorialIntent = new Intent(src.getContext(), WordBoxTutorial.class);
                startActivityForResult(tutorialIntent, 0);
                break;
            case R.id.ButtonScoresID:
                Intent scoresIntent = new Intent(src.getContext(), WordBoxScores.class);
                startActivityForResult(scoresIntent, 0);
                break;
            case R.id.ButtonGameID:
                Intent gameIntent = new Intent(src.getContext(), WordBoxGame.class);
                startActivityForResult(gameIntent, 0);
                break;
            case R.id.ButtonExitID:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
