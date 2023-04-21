package com.androidheroes.activities;

import com.androidheroes.R;
import com.androidheroes.views.MainGameView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class GameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_game);
        FrameLayout frame = (FrameLayout) findViewById(R.id.game_area);
        MainGameView image = new MainGameView(this);
        frame.addView(image);
    }
}
