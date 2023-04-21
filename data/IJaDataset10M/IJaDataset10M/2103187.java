package mp3Player;

/**
 * Base Class for audio output.
 */
public abstract class Obuffer {

    public static final int OBUFFERSIZE = 2 * 1152;

    public static final int MAXCHANNELS = 2;

    /**
   * Takes a 16 Bit PCM sample.
   */
    public abstract void append(int channel, short value);

    /**
   * Accepts 32 new PCM samples. 
   */
    public void appendSamples(int channel, float[] f) {
        short s;
        for (int i = 0; i < 32; ) {
            s = clip(f[i++]);
            append(channel, s);
        }
    }

    /**
   * Clip Sample to 16 Bits
   */
    private final short clip(float sample) {
        return ((sample > 32767.0f) ? 32767 : ((sample < -32768.0f) ? -32768 : (short) sample));
    }

    /**
   * Write the samples to the file or directly to the audio hardware.
   */
    public abstract void write_buffer(int val);

    public abstract void close();

    /**
   * Clears all data in the buffer (for seeking).
   */
    public abstract void clear_buffer();

    /**
   * Notify the buffer that the user has stopped the stream.
   */
    public abstract void set_stop_flag();
}
