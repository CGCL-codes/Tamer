package com.sesca.audio;

import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;
import com.sesca.misc.Logger;
import com.sun.org.apache.bcel.internal.generic.FREM;

public class AdaptiveSpeakerOutput implements AudioDestination {

    SourceDataLine line;

    double f1 = 1209;

    double f2 = 697;

    int index = 0;

    int buffering = 10;

    int frameLengthMillis = 20;

    long receivedFrames = 0;

    long timePlayed = 0;

    long startTime = 0;

    long t1 = 0;

    long t2 = 0;

    boolean initialFill = true;

    private void init(AudioFormat format) {
        DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(lineInfo)) {
            System.err.println("ERROR: AudioLine not supported by this System.");
        }
        try {
            line = (SourceDataLine) AudioSystem.getLine(lineInfo);
            line.open(format);
            Logger.debug("Line opened");
        } catch (LineUnavailableException e) {
            System.err.println("ERROR: LineUnavailableException at AudioReceiver()");
            e.printStackTrace();
        }
        if (!line.isOpen()) {
            Logger.error("Linja on kiinni");
        }
    }

    public void init() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, 1, 2, 8000, false);
        init(format);
    }

    public void close() {
        line.drain();
        line.close();
        long t = System.currentTimeMillis() - startTime;
        System.out.println("Speaker output stopped.");
    }

    public void init(AudioDestinationListener listener, AudioFormat format, int frameSize) {
        init();
    }

    public void go() {
    }

    public void onReceivedDestinationFrame(byte[] b) {
        receivedFrames++;
        t2 = System.currentTimeMillis();
        long t = t2 - startTime;
        long latency = t2 - t1;
        t1 = t2;
        if (receivedFrames * frameLengthMillis % 1000 == 0) {
        }
        if (line == null) Logger.error("Line=null");
        int a = line.available();
        int s = line.getBufferSize();
        if (s - a == 0 && buffering == -1) {
            line.stop();
            buffering = 5;
        }
        line.write(b, 0, b.length);
        if (buffering == 0) {
            line.start();
            buffering--;
            if (initialFill) {
                initialFill = false;
                startTime = System.currentTimeMillis();
            }
        }
        if (buffering >= 0) {
            buffering--;
        }
    }

    public void stop() {
        if (line.isOpen()) {
            line.drain();
            line.stop();
        } else {
            System.err.print("WARNING: Audio stop error: source line is not open.");
        }
    }

    public void play() {
        if (line.isOpen()) line.start(); else {
            System.err.print("WARNING: Audio play error: source line is not open.");
        }
    }
}
