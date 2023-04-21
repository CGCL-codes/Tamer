package org.example.audio;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;

public class Audio extends Activity {

    /** Called when the activity is first created. */
    private MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int resId;
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                resId = R.raw.bee;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                resId = R.raw.bird;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                resId = R.raw.bird_2;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                resId = R.raw.cat_1;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                resId = R.raw.crow;
                break;
            case KeyEvent.KEYCODE_A:
                resId = R.raw.dog_bark;
                break;
            case KeyEvent.KEYCODE_S:
                resId = R.raw.dogs;
                break;
            case KeyEvent.KEYCODE_D:
                resId = R.raw.elephant;
                break;
            case KeyEvent.KEYCODE_F:
                resId = R.raw.birds_sing;
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        if (mp != null) {
            mp.release();
        }
        mp = MediaPlayer.create(this, resId);
        mp.start();
        return true;
    }
}
