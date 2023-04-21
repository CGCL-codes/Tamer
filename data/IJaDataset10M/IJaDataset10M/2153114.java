package com.sun.mmedia;

/**
 * Parent interface that should be implemented by all DataSinks. 
 * It does not define any methods but only constants for the known
 * DataSink types
 */
public interface PCMAudioOut {

    /**
     * Default DataSink type 
     */
    public final int DATASINK_DEFAULT = 0;

    /**
     * Sound 3D DataSink type 
     */
    public final int DATASINK_SOUND3D = 1;

    /**
     * Media Processor DataSink
     */
    public final int DATASINK_EFFECTS = 2;

    /**
     * Open connection to the DataSink with the given parameters.
     *
     * @param sampleRate sample rate of the outpus stream
     * @param bits       number bits per channel
     * @param channels   number of channels in the stream, e.g. 1 for mono, 
     *                   2 for stereo, etc.
     */
    public boolean open(int sampleRate, int bits, int channels);

    public boolean open(int sampleRate, int bits, int channels, boolean isSigned, boolean isBigEndian);

    /**
     * Writes data to the data sink
     */
    public int write(byte[] data, int offset, int len);

    /**
     * Pauses the data processing by the data sink
     */
    public void pause();

    /**
     * Resumes the data processing by the data sink
     */
    public void resume();

    /**
     * Flashes any data buffered in the data sink buffers
     */
    public void flush();

    public int drain();

    public void drainLoop();

    public long getSamplesPlayed();

    public int getVolume();

    public void setVolume(int level);

    public long getMediaTime();

    public void setMediaTime(long mediaTime);

    public void setRate(int rate);

    public void close();
}
