package com.ajoniec.rhytm.thread;

import com.ajoniec.utils.Utils;
import com.ajoniec.*;
import com.ajoniec.res.MetronomeResources;
import com.ajoniec.rhytm.Rhytm;
import com.ajoniec.rhytm.RhytmNote;
import java.io.ByteArrayInputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

/**
 *
 * @author Adam Joniec
 */
public class RhytmThread extends Thread {

    private static Configuration conf = Configuration.instance();

    private static ByteArrayInputStream baisBeatWave = null, baisAccentedBeatWave = null;

    private static Player playerAccentedBeat = null, playerBeat = null;

    private static RhytmThread rhytmTread = new RhytmThread();

    boolean play = false, end = false;

    private boolean threadStarted = false;

    private Rhytm rhytm = null;

    private RhytmThreadInterface callback = null;

    private long timeStart, timeElapsed, timeOld;

    private RhytmThread() {
        recreatePlayers();
        System.out.println("RhytmThread()");
    }

    public static RhytmThread instance() {
        System.out.println("instance()");
        return rhytmTread;
    }

    private void recreatePlayers() {
        try {
            baisBeatWave = Utils.readISToBAIS(MetronomeResources.instance().getBeatAsStream(conf.getBeatHeader(), false));
            baisAccentedBeatWave = Utils.readISToBAIS(MetronomeResources.instance().getBeatAsStream(conf.getBeatHeader(), true));
            playerAccentedBeat = Manager.createPlayer(baisAccentedBeatWave, "audio/X-wav");
            playerBeat = Manager.createPlayer(baisBeatWave, "audio/X-wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Rhytm rhytm, boolean play, RhytmThreadInterface callback) {
        if (threadStarted) {
            return;
        }
        this.rhytm = rhytm;
        this.callback = callback;
        timeStart = System.currentTimeMillis();
        timeElapsed = 0;
        System.out.println(rhytm);
        if (play) {
            play();
        }
    }

    public void changeRhytm(Rhytm newRhytm) {
    }

    private void playBeat(RhytmNote note, int soundSource) {
        try {
            if (!note.isPlayed()) {
                return;
            }
            try {
                playerAccentedBeat.stop();
                playerBeat.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch(soundSource) {
                case Configuration.SOUND_BEAT:
                    if (note.isAccented()) {
                        playerAccentedBeat.start();
                    } else {
                        playerBeat.start();
                    }
                    break;
                case Configuration.SOUND_TONE:
                    if (note.isAccented()) {
                        Manager.playTone(conf.getToneAccentedBeat(), 100, 80);
                    } else {
                        Manager.playTone(conf.getToneBeat(), 70, 80);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            recreatePlayers();
        }
    }

    public void run() {
        if (!threadStarted) {
            return;
        }
        long flashBacklightTime = System.currentTimeMillis();
        while (true) {
            try {
                if (play) {
                    RhytmNote note = null;
                    int playedBar, playedBeat;
                    synchronized (rhytm) {
                        note = new RhytmNote(rhytm.getPlayedNote());
                        playedBar = rhytm.getPlayedBar();
                        playedBeat = rhytm.getPlayedBeat();
                        System.out.println(note.toString() + rhytm.getPlayedBeat() + ":" + rhytm.getPlayedBar());
                        rhytm.playNextNote();
                    }
                    callback.onNextBeat(note, playedBar, playedBeat);
                    playBeat(note, conf.getSoundSource());
                    note = null;
                }
                try {
                    int tempo = 90;
                    synchronized (rhytm) {
                        tempo = rhytm.getTempoVal(getRunningTime());
                    }
                    if ((getRunningTime() / 1000) != (timeOld / 1000)) {
                        long timeNew = getRunningTime();
                        callback.onTimeChange(timeOld, timeNew);
                        timeOld = timeNew;
                    }
                    Thread.sleep(1000 * 60 / tempo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (play && conf.getFlashBacklightEvery() > 0) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > (flashBacklightTime + (conf.getFlashBacklightEvery() * 1000))) {
                        callback.onFlashBacklight();
                        flashBacklightTime = currentTime;
                    }
                }
                if (end) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void pause() {
        if (play == false) {
            return;
        }
        timeElapsed += (System.currentTimeMillis() - timeStart);
        play = false;
    }

    public void play() {
        if (rhytm == null || play == true) {
            return;
        }
        play = true;
        timeStart = System.currentTimeMillis();
        if (!threadStarted) {
            threadStarted = true;
            start();
            System.out.print(" started\n");
        }
    }

    public long getRunningTime() {
        long time = timeElapsed + (play ? (System.currentTimeMillis() - timeStart) : 0);
        return time;
    }

    public void setRunningTime(long time) {
        timeElapsed = 0;
        timeStart = System.currentTimeMillis();
    }

    public void stop() {
        pause();
        synchronized (rhytm) {
            rhytm.startPlaying();
        }
    }

    public void end() {
        end = true;
    }
}
