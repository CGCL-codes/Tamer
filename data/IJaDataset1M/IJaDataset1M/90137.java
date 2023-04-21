package com.cookeroo.media;

import java.nio.BufferOverflowException;

/**
 * The <code>DecoderInterface</code> has to be implemented by every
 * decoder implementation in OpenCookeroo.
 * @author Thomas Quintana
 */
public interface DecoderInterface extends CodecInterface {

    /**
	 * Decodes one frame of data contained in a byte array.
	 * @param frame
	 */
    public void decode(byte frame[]) throws BufferOverflowException;

    /**
	 * @return The number of samples processed.
	 */
    public int getAvailableSampleLength();

    /**
	 * @return The state of the decoder.
	 */
    public boolean isDecoding();
}
