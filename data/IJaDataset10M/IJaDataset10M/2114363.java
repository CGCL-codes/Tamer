package edu.washington.assist.audio.wav;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class LineManager implements Runnable {

    private final SourceDataLine line;

    private volatile boolean running = false;

    private final BlockingQueue<SoundChunk> bufferQueue = new LinkedBlockingQueue<SoundChunk>();

    private Thread audioThread = null;

    public static final int BYTES_TO_WRITE = 512;

    public LineManager(AudioFormat format) throws LineUnavailableException {
        line = AudioSystem.getSourceDataLine(format);
        line.open();
    }

    public void start() {
        if (running) return;
        this.running = true;
        line.start();
        audioThread = new Thread(this);
        audioThread.start();
    }

    public void flush() {
        if (!running) return;
        line.flush();
        bufferQueue.clear();
    }

    public void stop() {
        if (!running) return;
        running = false;
        line.stop();
        line.flush();
        bufferQueue.clear();
        audioThread.interrupt();
        audioThread = null;
    }

    public void run() {
        while (running) {
            try {
                SoundChunk chunk = bufferQueue.take();
                int bytesWritten = 0;
                int numBytes = chunk.data.length - chunk.offset;
                while (running && (bytesWritten < numBytes)) {
                    int allotment = Math.min(BYTES_TO_WRITE, (numBytes - bytesWritten));
                    bytesWritten += line.write(chunk.data, chunk.offset + bytesWritten, allotment);
                }
            } catch (InterruptedException e) {
                System.out.println("Audio playback interrupted!");
            }
        }
        System.out.println("LineManager thread exiting...");
    }

    public void enqueueData(byte[] data, int byteOffset) {
        try {
            bufferQueue.add(new SoundChunk(data, byteOffset));
        } catch (IllegalStateException ise) {
            System.err.println("Warning: over-flowed audio buffer!");
        }
    }

    public void dispose() {
        this.stop();
        line.close();
    }
}
