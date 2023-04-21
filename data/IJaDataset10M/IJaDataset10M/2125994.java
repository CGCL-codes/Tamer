package org.xmlvm.tutorial.android.sound;

import java.io.IOException;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * This application demonstrates the use of the MediaPlayer. The application has
 * a single activity : SoundActivity. A button is provided which allows the user
 * to start and stop the audio playback. A switch is provided to enable/disable
 * the looping of playback. If the looping is enabled the MediaPlayer remains in
 * the 'started' state after the playback has reached the end of the stream. If
 * the looping is not enabled by the user, then the callback method
 * <code>onCompletion()</code> which is registered using
 * <code>setOnCompletionListener()</code> is called. This is when the
 * MediaPlayer is in 'PlaybackCompleted' state.
 * 
 * The MediaPlayer has various states. When the MediaPlayer is created using
 * <code>create()</code> then the it is already in 'prepared' state. If the
 * MediaPlayer is created using 'new' then it is in 'Idle' state and it has to
 * be 'prepared' by calling 'prepare' before it can enter the 'Started' state.
 * The MediaPlayer enters the 'Stopped' state by making call to
 * <code>stop()</code>. When the looping is not enabled and when
 * <code>OnCompletion()</code> is invoked, the MediaPlayer enters the
 * 'PlaybackCompleted' state. The MediaPlayer can also enter 'Paused' state.
 */
public class SoundActivity extends Activity {

    private CheckBox loopSwitch;

    private MediaPlayer mediaPlayer;

    private Button button;

    private boolean playing = false;

    private boolean first = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
    }

    public void setupUI() {
        setContentView(R.layout.main);
        button = (Button) findViewById(R.id.button);
        button.setText("Start playing...");
        loopSwitch = (CheckBox) findViewById(R.id.loop_sound_switch);
        loopSwitch.setChecked(true);
        loopSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mediaPlayer.setLooping(isChecked);
            }
        });
        mediaPlayer = MediaPlayer.create(this, R.raw.rain_thunders);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                button.setText("Start playing ...");
                playing = false;
                first = true;
            }
        });
    }

    public void onClick(View view) {
        if (playing) {
            button.setText("Start playing ...");
            playing = false;
            mediaPlayer.stop();
        } else {
            button.setText("Stop playing ...");
            playing = true;
            mediaPlayer.setLooping(loopSwitch.isChecked() ? true : false);
            if (first == true) first = false; else {
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mediaPlayer.start();
        }
    }

    public void stopPlaying() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                playing = false;
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaying();
    }
}
