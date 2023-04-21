package com.vnpgame.undersea.services;

import java.util.Random;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import com.vnpgame.chickenmerrychristmas.R;

public class LocalService extends Service {

    private final IBinder mBinder = new LocalBinder();

    private final Random mGenerator = new Random();

    private static MediaPlayer player = null;

    public class LocalBinder extends Binder {

        LocalService getService() {
            return LocalService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getRandomNumber() {
        if (player == null) {
            player = MediaPlayer.create(getBaseContext(), R.raw.background);
        }
        if (player.isPlaying()) {
            player.pause();
        } else {
            player.start();
        }
        return mGenerator.nextInt(100);
    }
}
