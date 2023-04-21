package net.url404.umodular.components;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import net.url404.umodular.*;

/**
 * Sample playback component.
 *
 * TODO: currently supports only samples of framesize 4bytes
 * (16bit stereo samples)<br>
 * TODO: MODE_LOOP_TOGGLE not implemented
 * <p>
 * INPUT 0: trigger (gate)<br>
 * INPUT 1: sample playback frequency (optional)
 * <p>
 * OUTPUT 0: output signal (SignalComponentConnector)
 *
 * @author      makela@url404.net
 */
public class SamplerComponent extends SoundComponent {

    /** output signal connector */
    private SignalComponentConnector outSignal;

    /** audio filename */
    private String filename = "";

    /** audio stream */
    private double[] sampleMemory = null;

    private int streamLength = 0;

    private double baseFreq = 0;

    private int channels = 0;

    /** sample playback frequency, internal */
    private double playbackFreq = 44100.0;

    /** playback */
    public static final int MODE_TRIGGER = 1;

    public static final int MODE_GATE = 2;

    public static final int MODE_LOOP = 3;

    public static final int MODE_LOOP_TOGGLE = 4;

    private double playbackPtr = 0.0;

    private double playbackSpeed = 0.0;

    private double playbackStart = 0.0;

    private double loopStart = 0.0;

    private double loopEnd = 0.0;

    private int playbackGate = 0;

    private double prevGate = 0.0;

    private int playbackMode = MODE_TRIGGER;

    /** modulation */
    public static final int MOD_OFF = 0;

    public static final int MOD_LOOPSTART = 1;

    private int modMode = MOD_LOOPSTART;

    private double modAmount = 2048.0;

    /**
   * Constructor
   */
    public SamplerComponent() {
        this.outSignal = new SignalComponentConnector(this);
        this.attachOutput(0, this.outSignal);
        this.vendorName = "U4SMPL-1";
        this.numInputConnectors = 3;
        setInputConnectorName(0, "trig");
        setInputConnectorName(1, "freq");
        setInputConnectorName(2, "mod");
    }

    /**
   * Constructor
   */
    public SamplerComponent(double sampleRate) {
        this.outSignal = new SignalComponentConnector(this);
        this.attachOutput(0, this.outSignal);
        this.vendorName = "U4SMPL-1";
        setSampleRate(sampleRate);
        this.numInputConnectors = 3;
        setInputConnectorName(0, "trig");
        setInputConnectorName(1, "freq");
        setInputConnectorName(2, "mod");
    }

    /**
   *
   * Read list of next values. See SoundComponent read() method.
   *
   * @return              Next values
   */
    public double[] read(int portNum) {
        return outputBuf;
    }

    /**
   *
   * Advance implementation. See SoundComponent advance() method.
   *
   * @return              Next values
   * @throws IOException  Bad wiring
   */
    public void advance() throws IOException {
        if (sampleMemory == null || streamLength == 0) {
            return;
        }
        boolean freqConnect = false;
        boolean gateConnect = false;
        double freqArray[] = new double[1];
        double gateArray[] = new double[1];
        if (getInput(1) != null) {
            freqConnect = true;
            freqArray = getInput(1).read();
        }
        if (getInput(0) != null) {
            gateConnect = true;
            gateArray = getInput(0).read();
        }
        switch(playbackMode) {
            case MODE_TRIGGER:
                advanceTriggered(freqConnect, gateConnect, freqArray, gateArray);
                break;
            case MODE_GATE:
                advanceGated(freqConnect, gateConnect, freqArray, gateArray);
                break;
            case MODE_LOOP:
                advanceLooped(freqConnect, gateConnect, freqArray, gateArray);
                break;
        }
    }

    /**
   * Advance implementation for MODE_TRIGGER. {{{
   */
    private void advanceTriggered(boolean freqConn, boolean gateConn, double freqArr[], double gateArr[]) {
        for (int i = 0; i < UModularProperties.READ_BUFFER_SIZE; i++) {
            if (gateConn && prevGate != 1.0 && gateArr[i] == 1.0) {
                playbackPtr = 0.0;
                playbackGate = 1;
            }
            prevGate = gateArr[i];
            if (playbackGate == 1) {
                outputBuf[i] = sampleMemory[(int) playbackPtr];
                double playHz = playbackFreq;
                if (freqConn) {
                    playHz = freqArr[i];
                }
                playbackSpeed = playHz / baseFreq;
                playbackPtr += playbackSpeed;
                if ((int) playbackPtr >= streamLength) {
                    playbackPtr = 0.0;
                    playbackGate = 0;
                }
            } else {
                outputBuf[i] = 0.0;
            }
        }
    }

    /**
   * Advance implementation for MODE_GATE. {{{
   */
    private void advanceGated(boolean freqConn, boolean gateConn, double freqArr[], double gateArr[]) {
        for (int i = 0; i < UModularProperties.READ_BUFFER_SIZE; i++) {
            if (gateConn) {
                if (gateArr[i] == 1.0) {
                    playbackGate = 1;
                } else {
                    playbackGate = 0;
                    playbackPtr = 0.0;
                }
            }
            prevGate = gateArr[i];
            if (playbackGate == 1) {
                if ((int) playbackPtr < streamLength) {
                    outputBuf[i] = sampleMemory[(int) playbackPtr];
                    double playHz = playbackFreq;
                    if (freqConn) {
                        playHz = freqArr[i];
                    }
                    playbackSpeed = playHz / baseFreq;
                    playbackPtr += playbackSpeed;
                } else {
                    outputBuf[i] = 0.0;
                }
            } else {
                outputBuf[i] = 0.0;
            }
        }
    }

    /**
   * Advance implementation for MODE_LOOP. {{{
   */
    private void advanceLooped(boolean freqConn, boolean gateConn, double freqArr[], double gateArr[]) {
        boolean modConn = (getInput(2) != null ? true : false);
        double modArr[] = new double[1];
        if (modConn) {
            modArr = getInput(2).read();
        }
        for (int i = 0; i < UModularProperties.READ_BUFFER_SIZE; i++) {
            if (gateConn) {
                if (gateArr[i] == 1.0) {
                    playbackGate = 1;
                } else {
                    playbackGate = 0;
                    playbackPtr = 0.0;
                }
            }
            prevGate = gateArr[i];
            if (playbackGate == 1) {
                outputBuf[i] = sampleMemory[(int) playbackPtr];
                double playHz = playbackFreq;
                if (freqConn) {
                    playHz = freqArr[i];
                }
                playbackSpeed = playHz / baseFreq;
                playbackPtr += playbackSpeed;
                double loopStartMod = 0.0;
                if (modMode == MOD_LOOPSTART && modConn) {
                    loopStartMod = modAmount * modArr[i];
                }
                if ((int) playbackPtr >= loopEnd || (int) playbackPtr >= streamLength) {
                    playbackPtr = loopStart + loopStartMod;
                    if (playbackPtr >= loopEnd - 1.0) {
                        playbackPtr = loopEnd - 1.0;
                    }
                }
            } else {
                outputBuf[i] = 0.0;
            }
        }
    }

    /**
   * Open audio file.
   *
   * @param filename Audio filename
   */
    public void openAudioFile(String filename) {
        this.filename = filename;
        File soundFile = new File(filename);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat audioFormat = audioStream.getFormat();
            baseFreq = (double) audioFormat.getSampleRate();
            channels = audioFormat.getChannels();
            int avail = audioStream.available();
            int frameSize = audioFormat.getFrameSize();
            byte[] samples = new byte[avail];
            int r = audioStream.read(samples, 0, avail);
            streamLength = avail / frameSize;
            sampleMemory = new double[streamLength];
            for (int i = 0; i < streamLength; i++) {
                int ptr = i * frameSize;
                int leftValue = samples[ptr + 0];
                leftValue += samples[ptr + 1] << 8;
                int rightValue = samples[ptr + 2];
                rightValue += samples[ptr + 3] << 8;
                int nValue = (leftValue + rightValue) / 2;
                sampleMemory[i] = ((double) nValue / (16 * 16 * 16 * 16));
            }
            DebugTools.msg(70, "opened " + filename + ": " + streamLength + " words");
            playbackStart = 0.0;
            loopStart = 0.0;
            loopEnd = streamLength;
            playbackMode = MODE_TRIGGER;
            audioStream.close();
        } catch (Exception e) {
            return;
        }
    }

    /**
   * Set internal playback frequency.
   *
   * @param f   Frequency in Hz
   */
    public void setPlaybackFrequency(double f) {
        playbackFreq = f;
    }

    /**
   * Get internal playback frequency.
   *
   * @return    Playback freq in Hz
   */
    public double getPlaybackFrequency() {
        return playbackFreq;
    }

    /**
   * Get loaded sample filename.
   */
    public String getSampleFilename() {
        return filename;
    }

    /**
   * Set playback mode
   */
    public void setPlaybackMode(int m) {
        playbackMode = m;
    }

    /**
   * Get playback mode
   */
    public int getPlaybackMode() {
        return playbackMode;
    }

    /**
   * Set loop point start
   */
    public void setLoopStart(double d) {
        loopStart = d;
    }

    /**
   * Get loop point start
   */
    public double getLoopStart() {
        return loopStart;
    }

    /**
   * Set loop point start in percents
   */
    public void setLoopStartPro(double d) {
        if (d < 0.0 || d > 100.0) {
            return;
        }
        loopStart = (d / 100.0) * (double) streamLength;
    }

    /**
   * Get loop point start in percents
   */
    public double getLoopStartPro() {
        return (loopStart / (double) streamLength) * 100.0;
    }

    /**
   * Set loop point end
   */
    public void setLoopEnd(double d) {
        loopEnd = d;
    }

    /**
   * Get loop point end
   */
    public double getLoopEnd() {
        return loopEnd;
    }

    /**
   * Set loop point end in percents
   */
    public void setLoopEndPro(double d) {
        if (d < 0.0 || d > 100.0) {
            return;
        }
        loopEnd = (d / 100.0) * (double) streamLength;
    }

    /**
   * Get loop point end in percents
   */
    public double getLoopEndPro() {
        return (loopEnd / (double) streamLength) * 100.0;
    }

    /**
   * Return a XML string representation of the component.
   */
    public String toXML() {
        String modeStr = "";
        switch(playbackMode) {
            case MODE_TRIGGER:
                modeStr = "trigger";
                break;
            case MODE_GATE:
                modeStr = "gate";
                break;
            case MODE_LOOP:
                modeStr = "loop";
                break;
            case MODE_LOOP_TOGGLE:
                modeStr = "looptoggle";
                break;
        }
        return "<sampler freq=\"" + playbackFreq + "\" " + "filename=\"" + filename + "\" " + "mode=\"" + modeStr + "\" " + "loopstart=\"" + loopStart + "\" loopend=\"" + loopEnd + "\"/>";
    }

    /**
   * Initialize component
   */
    public void clear() {
        super.clear();
    }
}
