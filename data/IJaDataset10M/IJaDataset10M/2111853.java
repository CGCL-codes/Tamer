package org.jnetpcap.util;

/**
 * Gets an offset for the object.
 * 
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public interface Offset {

    /**
	 * Offset of the object
	 * 
	 * @return a zero based offset
	 */
    public int offset();
}
