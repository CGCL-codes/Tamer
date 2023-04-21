package org.ct42.r42.imagedestination;

/**
 * Thrown while any eror occurs in an image destination.
 * 
 * @author cthiele
 */
public class DestinationException extends RuntimeException {

    /**
	 * @param message
	 */
    public DestinationException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public DestinationException(String message, Throwable cause) {
        super(message, cause);
    }
}
