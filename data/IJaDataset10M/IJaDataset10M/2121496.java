package com.triplyx.volume;

import java.io.InputStream;

public interface DualKeyVolume extends Volume {

    /**
	 * Sets the source of one-time pad bytes that is used if the volume
	 * is written to. If the volume will never be written to, then this
	 * method does not need to be called.
	 * @param oneTimePadStream
	 */
    public void setOneTimePadSource(InputStream oneTimePadStream);
}
