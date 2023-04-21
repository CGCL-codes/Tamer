package android.com.abb;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import junit.framework.Assert;

public class AlienBloodBathMain extends Activity {

    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        Content.initialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mGameState = new GameState(this, this);
        mGameView = (GameView) findViewById(R.id.game_view);
        mGameView.setGame(mGameState);
        Intent intent = getIntent();
        int level_index = Integer.parseInt(intent.getAction());
        Uri level_directory = intent.getData();
        if (saved_instance_state != null) {
            mGameState.loadStateBundle(saved_instance_state.getBundle("mGameState"));
        } else {
            mGameState.map.loadFromUri(level_directory, level_index);
            mGameState.reset();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return AlienBloodBathActivity.onCreateOptionsMenu(this, menu);
    }

    @Override
    public boolean onMenuItemSelected(int feature_id, MenuItem item) {
        return AlienBloodBathActivity.onMenuItemSelected(this, feature_id, item);
    }

    @Override
    public void onSaveInstanceState(Bundle saved_instance_state) {
        saved_instance_state.putBundle("mGameState", mGameState.saveStateBundle());
        super.onSaveInstanceState(saved_instance_state);
    }

    @Override
    public void onRestoreInstanceState(Bundle saved_instance_state) {
        super.onRestoreInstanceState(saved_instance_state);
        mGameState.loadStateBundle(saved_instance_state.getBundle("mGameState"));
    }

    private GameState mGameState;

    private GameView mGameView;
}
