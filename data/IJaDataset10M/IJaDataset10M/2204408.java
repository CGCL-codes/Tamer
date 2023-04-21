package com.echodrama.upturner;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;

public class SoundPollManager {

    private static Context context;

    private static SoundPool spool;

    private static Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    public static void initialize(Context pContext) {
        context = pContext;
        spool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 10);
    }

    public static void play(final int soundId) {
        if (soundMap.containsKey(soundId)) {
            spool.play(soundMap.get(soundId), 1, 1, 1, 0, 1);
        } else {
            int sound = spool.load(context, soundId, 0);
            soundMap.put(soundId, sound);
            final Timer timer = new Timer();
            final Handler handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    SoundPollManager.play(soundId);
                    super.handleMessage(msg);
                }
            };
            final TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    Message message = new Message();
                    handler.sendMessage(message);
                }
            };
            timer.schedule(task, 1000);
        }
    }

    public static void stop(final int soundId) {
        if (soundMap.containsKey(soundId)) {
            spool.stop(soundMap.get(soundId));
        } else {
            return;
        }
    }
}
