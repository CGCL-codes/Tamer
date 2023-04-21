package dsp.soundinput;

import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import dsp.DataChunk;
import dsp.RealTimeInput;
import dsp.SingleDataChunk;
import dsp.VariableDataChunk;
import dsp.exception.RecoverableInputProblem;

/**
 * @author canti, 23.11.2004
 *  
 */
public class SimpleAudioInput implements RealTimeInput {

    private float sampleRate;

    private int sampleSizeInBits;

    private int channels;

    private boolean bigEndian;

    AudioFormat audioFormat;

    SimpleAudioRecorder recorder;

    public SimpleAudioInput(float pSampleRate, int pSampleSizeInBits, int pChannels, boolean pSigned, boolean pBigEndian) {
        sampleRate = pSampleRate;
        sampleSizeInBits = pSampleSizeInBits;
        channels = pChannels;
        bigEndian = pBigEndian;
        audioFormat = new AudioFormat((pSigned) ? AudioFormat.Encoding.PCM_SIGNED : AudioFormat.Encoding.PCM_UNSIGNED, sampleRate, sampleSizeInBits, channels, (sampleSizeInBits / 8) * pChannels, sampleRate, bigEndian);
    }

    public boolean endOfInput() {
        return recorder.stopCapture;
    }

    public DataChunk getNext() throws RecoverableInputProblem {
        DataChunk nextSample = null;
        try {
            nextSample = (VariableDataChunk) (recorder.blockingQueue.take());
        } catch (SoundBlockingQueue.Closed e) {
            e.printStackTrace();
            throw new RecoverableInputProblem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (nextSample != null) return nextSample; else {
            throw new RecoverableInputProblem();
        }
    }

    public void start() {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = null;
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            System.out.println("unable to get a recording line");
            throw new RuntimeException(e);
        }
        recorder = new SimpleAudioRecorder(targetDataLine, AudioFileFormat.Type.WAVE);
        recorder.startRecording();
    }

    public void stop() {
        recorder.stopRecording();
    }

    public int getFrequency() {
        return (int) (sampleRate);
    }

    public float getDelayCorrection() {
        return 0f;
    }
}

class SimpleAudioRecorder extends Thread {

    private TargetDataLine targetLine;

    private AudioFileFormat.Type targetType;

    private AudioInputStream audioInputStream;

    boolean stopCapture;

    SoundBlockingQueue blockingQueue;

    public SimpleAudioRecorder(TargetDataLine line, AudioFileFormat.Type pTargetType) {
        targetLine = line;
        audioInputStream = new AudioInputStream(line);
        targetType = pTargetType;
        stopCapture = false;
        blockingQueue = new SoundBlockingQueue();
    }

    /**
     * Starts the recording. To accomplish this, (i) the line is started and
     * (ii) the thread is started.
     */
    public void startRecording() {
        targetLine.start();
        System.out.println("Recording from the Sound card..." + targetLine.toString());
        super.start();
    }

    public void stopRecording() {
        stopCapture = true;
    }

    public void run() {
        try {
            int frameSize = targetLine.getFormat().getFrameSize();
            byte[] buffer = new byte[1000];
            while (!stopCapture) {
                int countBytes = audioInputStream.read(buffer, 0, buffer.length);
                convertInputData(buffer);
            }
            targetLine.stop();
            targetLine.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertInputData(byte[] buffer) {
        AudioFormat.Encoding afe = targetLine.getFormat().getEncoding();
        AudioFormat format = targetLine.getFormat();
        int sampleSize = format.getSampleSizeInBits();
        int frameSize = format.getFrameSize();
        boolean bigEndian = format.isBigEndian();
        int channelNumber = format.getChannels();
        double[] data = new double[buffer.length / frameSize];
        double temp = 0;
        int j = 0;
        switch(sampleSize) {
            case 8:
                if (afe == AudioFormat.Encoding.PCM_SIGNED) for (int i = 0; i < buffer.length; i += channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += buffer[i + c];
                    data[j++] = temp / channelNumber;
                } else if (afe == AudioFormat.Encoding.PCM_UNSIGNED) for (int i = 0; i < buffer.length; i += channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += (byte) (buffer[i + c] ^ 0x80) + 128;
                    data[j++] = temp / channelNumber;
                }
                break;
            case 16:
                if (bigEndian) for (int i = 0; i < buffer.length; i += 2 * channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += (short) ((buffer[i + c] << 8) | (buffer[i + c + 1]));
                    data[j++] = (temp / channelNumber) / 256;
                } else for (int i = 0; i < buffer.length; i += 2 * channelNumber) {
                    temp = 0;
                    for (int c = 0; c < channelNumber; c++) temp += (short) ((buffer[i + c]) | (buffer[i + c + 1] << 8));
                    data[j++] = (temp / channelNumber) / 256;
                }
        }
        blockingQueue.add(new VariableDataChunk(data));
    }
}
