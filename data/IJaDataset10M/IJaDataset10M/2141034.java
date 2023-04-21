package org.anrc.poi;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private MediaPlayer mMediaPlayer;

    Notification notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        boolean bIn = b.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING, false);
        if (bIn) {
            Toast.makeText(context, "UWAGA ZBLIZASZ SIE DO RADARU !", Toast.LENGTH_LONG).show();
            try {
                if ((mMediaPlayer == null) || (!mMediaPlayer.isPlaying())) {
                    mMediaPlayer = MediaPlayer.create(context, R.raw.gong);
                    mMediaPlayer.start();
                    Log.i("MediaPlayer", "Powiadomiono o radarze OK");
                }
            } catch (Exception e) {
                Log.e("MediaPlayer", "error: " + e.getMessage(), e);
            }
        }
    }
}
