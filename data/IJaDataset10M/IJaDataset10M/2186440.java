package net.kano.partypad.datatypes;

import javax.sound.sampled.AudioFormat;
import java.nio.ShortBuffer;
import java.nio.ByteBuffer;

/**
 * A class for holding sound data.
 */
public class SoundBuffer {

    /** The audio format in which sound data is stored. */
    public static final AudioFormat AUDIO_FORMAT = new AudioFormat(44100.0f, 16, 2, true, true);

    /**
     * Returns the number of bytes in a sample in the current
     * {@linkplain #AUDIO_FORMAT format}.
     *
     * @return the number of bytes per sample
     */
    private static int getSampleSizeInBytes() {
        return AUDIO_FORMAT.getSampleSizeInBits() / 8;
    }

    /** The sound buffer. */
    private final byte[] bufferBytes;

    /** The sound buffer, as a short buffer. */
    private final ShortBuffer buffer;

    /**
     * Creates a new sound buffer with enough space for the given number of
     * samples.
     *
     * @param samples the number of samples
     */
    public SoundBuffer(int samples) {
        bufferBytes = new byte[samples * getSampleSizeInBytes()];
        buffer = ByteBuffer.wrap(bufferBytes).asShortBuffer();
    }

    /**
     * Returns the raw sound buffer.
     *
     * @return the raw sound buffer
     */
    public byte[] getBufferBytes() {
        return bufferBytes;
    }

    /**
     * Returns the raw sound buffer, in {@code ShortBuffer form}.
     *
     * @return the sound buffer
     */
    public ShortBuffer getBuffer() {
        return buffer;
    }

    /**
     * Returns the format of the audio stored in this buffer.
     *
     * @return this sound buffer's audio format
     */
    public AudioFormat getFormat() {
        return AUDIO_FORMAT;
    }
}
