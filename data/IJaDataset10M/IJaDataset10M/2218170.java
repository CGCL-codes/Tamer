package au.vermilion.PC;

import static au.vermilion.Vermilion.settings;
import static au.vermilion.Vermilion.logger;
import au.vermilion.audio.WavRecorder;
import au.vermilion.audio.AudioThreadBase;
import java.util.logging.Level;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * Encapsulates an audio output and the thread that feeds it. This class uses
 * the user's preferences to select the correct sound card, sample rate and
 * buffer size. Various constants to do with audio output are also defined
 * here.<br>
 * <br>
 * The audio thread allows registration of (at present) two providers. The
 * audio provider generates audio data and the event provider is able to regulate
 * audio production and pass commands to the audio provider. Thus the application
 * and preferences/document handling become a framework on which relatively loosely
 * coupled plugins can build the entire user interface and audio engine.
 */
public final class PCAudioThread extends AudioThreadBase implements Runnable {

    /**
     * The number of bytes per sample (per channel). Currently set to 2, for
     * 16-bit audio.
     */
    private static final int BYTES_PER_SAMPLE_PER_CHANNEL = 2;

    /**
     * The number of audio channels, currently 2, for stereo. Note we don't
     * often use this, so there are many places where two channels is assumed.
     */
    private static final int NUM_CHANNELS = 2;

    /**
     * The size of the mixing buffer in samples (per channel). The internal
     * buffer size is the most samples we will ever ask for at once.
     */
    private static final int internalBufferSize = 384;

    /**
     * The name (as given by Java) of the users preferred mixing device.
     */
    private String targetMixerName = null;

    /**
     * The output buffer size in milliseconds as chosen by the user preferences.
     */
    private int bufferSize = 200;

    /**
     * The output sample rate as chosen by the user preferences.
     */
    private int sampleRate = 22050;

    /**
     * When requested this can be used to stream the audio output to a file.
     * This is used to handle calls to openRecording and closeRecording.
     */
    private final WavRecorder recorder = new WavRecorder();

    /**
     * The size of the soundcard buffer in bytes (NOT per channel).
     */
    private int byteBufferSize = 0;

    /**
     * The external buffer, in the format of raw bytes.
     */
    private byte[] oBuffer = null;

    /**
     * The Java Sound class which accepts our audio data.
     */
    private SourceDataLine audioLine = null;

    /**
     * The mixer we are going to use (once we get it from prefs).
     */
    private Mixer targetMixer = null;

    /**
     * This is used to control the thread that feeds the audio line. The only
     * access to this is via close() which also terminates the audio thread and
     * closes the audio lines.
     */
    private boolean isRunning = false;

    /**
     * The empty constructor conforms to the standard for framework interface
     * implementations.
     */
    public PCAudioThread() {
    }

    /**
     * The initialisation reads the various audio output preferences and awaits a
     * call to start() which will attemp opening an audio line.
     */
    @Override
    public void initialise() {
        if (settings.valueExists("Audio/SampleRate") && settings.valueExists("Audio/BufferSize")) {
            try {
                sampleRate = ((Long) settings.getValue("Audio/SampleRate")).intValue();
                bufferSize = ((Long) settings.getValue("Audio/BufferSize")).intValue();
            } catch (Exception ex) {
                sampleRate = 22050;
                bufferSize = 50;
            }
        }
        int samplesPerBuffer = sampleRate * bufferSize / 1000;
        byteBufferSize = samplesPerBuffer * BYTES_PER_SAMPLE_PER_CHANNEL * NUM_CHANNELS;
        byteBufferSize -= byteBufferSize % 16;
        targetMixerName = (String) settings.getValue("Audio/SoundDevice");
        if (targetMixerName == null) targetMixerName = "";
    }

    /**
     * Creates an audio output and starts a thread to feed it data. This can fail
     * if the audio output cannot be opened with the given preferences. If we are
     * able to open an audio line we then start a thread in run().
     * @return True if the line is open and the thread started, otherwise false.
     */
    @Override
    public boolean start() {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        if (mixers.length < 1) {
            logger.log(Level.SEVERE, "NO AUDIO DEVICES!");
            return false;
        }
        Mixer.Info targetMixerInfo = mixers[0];
        for (int x = 0; x < mixers.length; x++) {
            if (mixers[x].toString().equals(targetMixerName)) targetMixerInfo = mixers[x];
        }
        targetMixer = AudioSystem.getMixer(targetMixerInfo);
        AudioFormat format = new AudioFormat(sampleRate, 16, NUM_CHANNELS, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            audioLine = (SourceDataLine) targetMixer.getLine(info);
            audioLine.open(format, byteBufferSize);
            logger.log(Level.INFO, "Opening audio output with latency {0}ms", (byteBufferSize * 1000 / sampleRate / BYTES_PER_SAMPLE_PER_CHANNEL / NUM_CHANNELS));
            audioLine.start();
            isRunning = true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to open audio line", ex);
            return false;
        }
        if (isRunning) {
            oBuffer = new byte[byteBufferSize];
            for (int x = 0; x < byteBufferSize; x++) {
                oBuffer[x] = 0;
            }
            Thread audioThread = new Thread(this);
            audioThread.setPriority(Thread.MAX_PRIORITY);
            audioThread.start();
            audioLine.write(oBuffer, 0, BYTES_PER_SAMPLE_PER_CHANNEL * NUM_CHANNELS);
        }
        return true;
    }

    /**
     * Runs the audio thread, an infinite loop that feeds the output when
     * needed. We simply mix as much as we can and block on the audio buffer
     * when we write the data.
     */
    @Override
    public void run() {
        while (isRunning) {
            try {
                int samplesProcessed = doMixing();
                int baseAddr = 0;
                for (int x = 0; x < samplesProcessed; x++) {
                    int bVal1 = (int) (buffer[0][x] * 32767.0f);
                    int bVal2 = (int) (buffer[1][x] * 32767.0f);
                    oBuffer[baseAddr] = (byte) (bVal1 & 0xFF);
                    oBuffer[baseAddr + 1] = (byte) ((bVal1 >>> 8) & 0xFF);
                    oBuffer[baseAddr + 2] = (byte) (bVal2 & 0xFF);
                    oBuffer[baseAddr + 3] = (byte) ((bVal2 >>> 8) & 0xFF);
                    baseAddr += 4;
                }
                if (recorder.isRecording) {
                    recorder.recordChunk(oBuffer, samplesProcessed);
                }
                audioLine.write(oBuffer, 0, baseAddr);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Audio thread crashed", ex);
                isRunning = false;
            }
        }
        for (int x = 0; x < sampleCountsIn.length; x++) {
            logger.log(Level.INFO, "Sample count history: {0}\t{1}", new Object[] { sampleCountsIn.data[x], sampleCountsOut.data[x] });
        }
    }

    /**
     * Stops the audio thread and then the output itself. Do not confuse this
     * with the act of stopping playback of cues.
     */
    @Override
    public void stop() {
        recorder.closeRecording();
        isRunning = false;
        if (audioLine != null) {
            audioLine.stop();
            audioLine.close();
        }
    }

    /**
     * Called when it is time to dispose of this class. At present this does
     * nothing.
     */
    @Override
    public void destroy() {
    }

    /**
     * Returns the size of the audio output buffer in samples per channel.
     */
    @Override
    public int getAudioBufferSize() {
        return internalBufferSize;
    }

    /**
     * Returns the size of the audio output buffer in samples per channel.
     */
    @Override
    public int getAudioLatency() {
        return bufferSize;
    }

    /**
     * Returns the sample rate in samples per second per channel.
     */
    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Returns the number of channels that are being mixed.
     */
    @Override
    public int getNumChannels() {
        return NUM_CHANNELS;
    }

    /**
     * Returns a list of audio devices for the user to choose from on
     * the preferences tab.
     */
    @Override
    public String[] getAvailableDevices() {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        String[] names = new String[mixers.length];
        for (int x = 0; x < mixers.length; x++) {
            Mixer m = AudioSystem.getMixer(mixers[x]);
            if (m.getSourceLineInfo().length > 0) {
                names[x] = (mixers[x].toString());
            }
        }
        return names;
    }

    /**
     * Returns a list of sample rates for the user to choose from on
     * the preferences tab.
     */
    @Override
    public int[] getAvailableSampleRates() {
        return new int[] { 4000, 8000, 11025, 16000, 22050, 32000, 44100, 48000, 64000, 96000 };
    }

    /**
     * Returns a list of buffer latencies for the user to choose from on
     * the preferences tab.
     */
    @Override
    public int[] getAvailableBufferLatencies() {
        return new int[] { 3, 5, 10, 15, 20, 35, 50, 75, 100, 150, 200, 350, 500 };
    }

    /**
     * Closes the audio output file stream if it is open.
     */
    @Override
    public void closeRecording() {
        recorder.closeRecording();
    }

    /**
     * Opens an audio output file stream to the specified path.
     * @param fileName The path to a file for recording the audio.
     */
    @Override
    public void openRecording(String fileName) {
        recorder.openRecording(fileName, sampleRate);
    }
}
