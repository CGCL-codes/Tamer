package com.cookeroo.media;

/**
 * The <code>CodecInterface</code> has to be implemented by every
 * coder/decoder implementation in OpenCookeroo.
 * @author Thomas Quintana
 */
public interface CodecInterface {

    /**
	 * Terminates the codec.
	 */
    public void close();

    /**
	 * @return The length of processed data in bytes.
	 */
    public int getAvailableByteLength();

    /**
	 * @return A byte array containing the processed data.
	 */
    public byte[] getBytes();

    /**
	 * @return The number of channels for the processed data.
	 */
    public int getChannels();

    /**
	 * @return An integer array containing the processed data.
	 */
    public int[] getInts();

    /**
	 * @return The sample rate for the processed data.
	 */
    public int getSampleRate();

    /**
	 * @return The samples size in bits for the processed data.
	 */
    public int getSampleSize();

    /**
	 * @return A short array containing the processed data.
	 */
    public short[] getShorts();

    /**
	 * @return The state of the processor thread.
	 */
    public boolean isRunning();

    /**
	 * Starts the data processor.
	 */
    public void start();

    /**
	 * Stops the data processor.
	 */
    public void stop();
}
