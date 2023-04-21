package com.multimedia002;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Multimedia002 extends Activity {

    private MediaPlayer mediaPlayer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
