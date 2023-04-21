package net.sf.mogbox.renderer.engine;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public abstract class Sound {

    public static final int BUFFER_SIZE = 4096;

    public static final int BUFFER_COUNT = 3;

    private static Logger log = Logger.getLogger(Sound.class.getName());

    private int channels;

    private int bitsPerSample;

    private int sampleRate;

    private int length;

    private int loopPoint = -1;

    private int alFormat;

    private int bytesPerSample;

    private int source = -1;

    private IntBuffer buffers;

    private ByteBuffer data;

    public Sound() {
        buffers = BufferUtils.createIntBuffer(BUFFER_COUNT + 1);
        data = BufferUtils.createByteBuffer(BUFFER_SIZE * bytesPerSample);
    }

    protected final void setFormat(int channels, int bitsPerSample, int sampleRate) {
        this.channels = channels;
        this.bitsPerSample = bitsPerSample;
        this.sampleRate = sampleRate;
        bytesPerSample = (bitsPerSample >> 3) * channels;
        alFormat = 0x1100 | (channels - 1 << 1) | (bitsPerSample >> 4);
    }

    protected final void setLength(int length) {
        this.length = length;
        loopPoint = -1;
    }

    protected final void setLength(int length, int loopPoint) {
        this.length = length;
        this.loopPoint = loopPoint;
    }

    public int getChannels() {
        return channels;
    }

    public int getBitsPerSample() {
        return bitsPerSample;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getLength() {
        return length;
    }

    public int getLoopPoint() {
        return loopPoint;
    }

    public double getDuration() {
        return (double) length / sampleRate;
    }

    public int getSource() {
        return source;
    }

    public final boolean play(int source) throws Exception {
        if (isPlaying() || !AL10.alIsSource(source)) return false;
        this.source = source;
        try {
            reset(false);
        } catch (Exception t) {
            log.log(Level.WARNING, null, t);
            return false;
        }
        buffers.clear().limit(BUFFER_COUNT);
        AL10.alGenBuffers(buffers);
        if (!stream(buffers.get(0))) return false;
        buffers.limit(1);
        AL10.alSourceQueueBuffers(source, buffers);
        for (int i = 1; i < BUFFER_COUNT; i++) {
            buffers.limit(i + 1);
            if (stream(buffers.get(i))) {
                buffers.position(i);
                AL10.alSourceQueueBuffers(source, buffers);
            } else {
                break;
            }
        }
        buffers.clear();
        AL10.alSourcePlay(source);
        return true;
    }

    public final void stop() {
        AL10.alSourceStop(source);
        source = -1;
    }

    public final boolean isPlaying() {
        if (!AL10.alIsSource(source)) return false;
        return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public final boolean update() throws Exception {
        if (source == -1) return false;
        boolean active = true;
        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
        if (processed > 0) {
            buffers.position(BUFFER_COUNT);
            while (processed-- > 0) {
                AL10.alSourceUnqueueBuffers(source, buffers);
                if (active = stream(buffers.get(BUFFER_COUNT))) AL10.alSourceQueueBuffers(source, buffers);
            }
            if (active && !isPlaying()) AL10.alSourcePlay(source);
        }
        active |= AL10.alGetSourcei(source, AL10.AL_BUFFERS_QUEUED) > 0;
        if (!active) source = -1;
        return active;
    }

    private boolean stream(int buffer) throws Exception {
        data.clear();
        boolean result = true;
        while (data.remaining() > getSamplesPerFrame() * bytesPerSample) {
            if (!load(data)) {
                if (loopPoint >= 0 && length > loopPoint) {
                    reset(true);
                } else {
                    result = false;
                    break;
                }
            }
        }
        data.flip();
        if (!data.hasRemaining()) return false;
        AL10.alBufferData(buffer, alFormat, data, sampleRate);
        return result;
    }

    protected abstract void reset(boolean loop) throws Exception;

    protected abstract boolean load(ByteBuffer data) throws Exception;

    protected abstract int getSamplesPerFrame() throws Exception;
}
