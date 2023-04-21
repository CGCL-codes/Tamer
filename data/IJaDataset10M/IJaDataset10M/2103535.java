package com.peterhi.nmedia.voice;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class PcmRecorder {

    private int bufferSegmentation = 50;

    private boolean playback;

    private AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000f, 16, 1, 2, 8000f, false);

    private TargetDataLine target;

    private SourceDataLine source;

    private RecorderListener listener;

    private CaptureThread captureThread;

    public PcmRecorder() {
    }

    public int getBufferSegmentation() {
        return bufferSegmentation;
    }

    public void setBufferSegmentation(int value) {
        bufferSegmentation = value;
    }

    public boolean isPlayback() {
        return playback;
    }

    public void setPlayback(boolean value) {
        if (captureThread != null) {
            if (captureThread.running) {
                throw new IllegalStateException("to enable/disable playback, " + "you must stop this recorder first.");
            }
        }
        playback = value;
    }

    public AudioFormat getAudioFormat() {
        return format;
    }

    public void setAudioFormat(AudioFormat value) {
        format = value;
    }

    public float getVolume() {
        return 0;
    }

    public void setVolume(float value) {
        FloatControl gainControl = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = value;
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public boolean isRunning() {
        return captureThread.running;
    }

    public RecorderListener getListener() {
        return listener;
    }

    public void setListener(RecorderListener value) {
        listener = value;
    }

    public synchronized boolean start() throws LineUnavailableException {
        if (captureThread != null) {
            if (captureThread.running) {
                return false;
            }
        }
        if (playback) {
            if ((source == null) || (!source.isOpen())) {
                DataLine.Info sourceLineInfo = new DataLine.Info(SourceDataLine.class, format);
                source = (SourceDataLine) AudioSystem.getLine(sourceLineInfo);
                source.open(format);
                source.start();
            }
        }
        if ((target == null) || (!target.isOpen())) {
            TargetDataLine.Info targetLineInfo = new TargetDataLine.Info(TargetDataLine.class, format);
            target = (TargetDataLine) AudioSystem.getLine(targetLineInfo);
            target.open(format);
            target.start();
        }
        if (captureThread == null) {
            captureThread = new CaptureThread();
        }
        captureThread.running = true;
        captureThread.start();
        return true;
    }

    public void stop() {
        if (captureThread != null) {
            captureThread.running = false;
            captureThread = null;
        }
        if (playback) {
            source.stop();
            source.drain();
        }
        target.stop();
        target.drain();
    }

    public void dispose() {
        if (playback) {
            source.close();
        }
        target.close();
    }

    public void dispatchRecorded(byte[] data, int length) {
        if (listener != null) {
            listener.recorded(this, data, length);
        }
    }

    class CaptureThread extends Thread {

        boolean running;

        byte[] buffer;

        CaptureThread() {
            super("audio capture thread");
            recalcBufferSize();
        }

        void recalcBufferSize() {
            int bufferSize = (int) format.getSampleRate() * format.getFrameSize() / getBufferSegmentation();
            buffer = new byte[bufferSize];
        }

        public void run() {
            while (running) {
                int count = PcmRecorder.this.target.read(buffer, 0, buffer.length);
                if (count > 0) {
                    if (playback) {
                        source.write(buffer, 0, count);
                    }
                    dispatchRecorded(buffer, count);
                }
            }
        }
    }
}
