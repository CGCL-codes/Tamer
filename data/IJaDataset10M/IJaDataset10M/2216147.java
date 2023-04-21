package com.sts.webmeet.content.client.audio;

import com.sts.webmeet.client.*;
import javax.sound.sampled.*;

public class NullSpeaker implements Speaker, Runnable {

    public void startPlaying(int iChannels, float fSampleRate, int iBitsPerSample, AtomicDataSource ads) throws Exception {
        this.ads = ads;
        threadSpeaker = new Thread(this, getClass().getName());
        bContinue = true;
        threadSpeaker.start();
    }

    public void run() {
        while (bContinue) {
            try {
                ads.getData();
            } catch (java.io.IOException ioe) {
                break;
            }
        }
        stopPlaying();
    }

    public int stopPlaying() {
        bContinue = false;
        return 0;
    }

    private AtomicDataSource ads;

    private boolean bContinue;

    private Thread threadSpeaker;
}
