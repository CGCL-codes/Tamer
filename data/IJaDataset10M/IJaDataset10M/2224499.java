package blue.soundObject.renderer.audioFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.SwingUtilities;

public class AudioWaveformCacheGenerator extends Thread {

    public static final String CACHE_GEN_COMPLETE = "CACHE_GEN_COMPLETE";

    Vector workQueue = new Vector();

    boolean running = true;

    boolean killed = false;

    private final AudioWaveformCache audioWaveformCache;

    public AudioWaveformCacheGenerator(AudioWaveformCache audioWaveformCache) {
        this.audioWaveformCache = audioWaveformCache;
    }

    public void addAudioWaveformData(AudioWaveformData audioWaveformData) {
        this.workQueue.add(audioWaveformData);
    }

    public int getPixelSeconds() {
        if (workQueue.isEmpty()) {
            return -1;
        }
        return ((AudioWaveformData) workQueue.get(0)).pixelSeconds;
    }

    public void run() {
        while (running && workQueue.size() > 0) {
            AudioWaveformData data = (AudioWaveformData) this.workQueue.get(0);
            analyzeWaveform(data);
            audioWaveformCache.fireAudioWaveformDataGenerated(data.fileName);
            if (running) {
                this.workQueue.remove(0);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
        if (!killed) {
            audioWaveformCache.fireAudioWaveformDataGenerated(CACHE_GEN_COMPLETE);
        }
        running = false;
    }

    public void killRunning() {
        running = false;
        killed = true;
        this.interrupt();
        workQueue.clear();
    }

    public boolean isRunning() {
        return running;
    }

    private void analyzeWaveform(final AudioWaveformData waveData) {
        try {
            File f = new File(waveData.fileName);
            AudioFileFormat aFormat = AudioSystem.getAudioFileFormat(f);
            AudioFormat format = aFormat.getFormat();
            int numChannels = format.getChannels();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(f)));
            int sr = (int) format.getSampleRate();
            int numBytesPerSample = audioInputStream.getFormat().getSampleSizeInBits() / 8;
            int numFramesToRead = sr / waveData.pixelSeconds;
            boolean bigEndian = format.isBigEndian();
            int len = format.getFrameSize() * numFramesToRead;
            byte[] dataBuffer = new byte[len];
            int maxWidth = (aFormat.getFrameLength() / numFramesToRead) + 1;
            waveData.data = new double[numChannels][maxWidth * 2];
            for (int i = 0; i < maxWidth && running; i++) {
                int numRead = audioInputStream.read(dataBuffer, 0, len);
                if (numRead <= 0) {
                    waveData.percentLoadingComplete = 1.0;
                    break;
                } else {
                    waveData.percentLoadingComplete = i / maxWidth;
                    if (i % 100 == 0) {
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                audioWaveformCache.fireAudioWaveformDataGenerated(waveData.fileName);
                            }
                        });
                    }
                }
                prepareSamples(waveData, dataBuffer, i, numChannels, numBytesPerSample, bigEndian);
            }
            waveData.percentLoadingComplete = 1.0;
            audioInputStream.close();
        } catch (UnsupportedAudioFileException e) {
            waveData.data = null;
            e.printStackTrace();
        } catch (IOException e) {
            waveData.data = null;
            e.printStackTrace();
        }
    }

    private void prepareSamples(AudioWaveformData waveForm, byte[] dataBuffer, int pixelNum, int numChannels, int numBytesPerSample, boolean bigEndian) {
        int frameSize = numChannels * numBytesPerSample;
        int scaleValue = 256;
        for (int i = 1; i < numBytesPerSample; i++) {
            scaleValue = scaleValue * 256;
        }
        scaleValue = scaleValue / 2;
        for (int i = 0; i < numChannels; i++) {
            int offset = i * numBytesPerSample;
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int j = 0; j < dataBuffer.length / frameSize; j++) {
                int index = (j * frameSize) + offset;
                int val = getValue(dataBuffer, index, numBytesPerSample, bigEndian);
                if (val < min) {
                    min = val;
                }
                if (val > max) {
                    max = val;
                }
            }
            double minY = min / (double) scaleValue;
            double maxY = max / (double) scaleValue;
            waveForm.data[i][pixelNum * 2] = minY;
            waveForm.data[i][pixelNum * 2 + 1] = maxY;
        }
    }

    private int getValue(byte[] dataBuffer, int offset, int numBytesPerSample, boolean bigEndian) {
        int value = 0;
        if (bigEndian) {
            int max = numBytesPerSample - 1;
            for (int i = 0; i < numBytesPerSample; i++) {
                int temp = dataBuffer[offset + i];
                int shiftVal = (max - i) * 8;
                value += temp << shiftVal;
            }
        } else {
            for (int i = 0; i < numBytesPerSample; i++) {
                int temp = dataBuffer[offset + i];
                int shiftVal = i * 8;
                value += temp << shiftVal;
            }
        }
        return value;
    }
}
