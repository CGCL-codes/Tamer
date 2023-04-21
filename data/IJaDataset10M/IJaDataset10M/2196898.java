package org.limaloa.samples.directapi.custombean;

/**
 * A checked exception used to indicate errors from methods in the target object.
 * 
 * @author Chris Nappin
 */
public class TargetException extends Exception {

    /**
	 * Creates a new instance.
	 * @param message  The error message
	 */
    public TargetException(String message) {
        super(message);
    }

    /**
	 * Creates a new instance.
	 * @param message   The error message
	 * @param ex		An exception to wrap
	 */
    public TargetException(String message, Throwable ex) {
        super(message, ex);
    }
}
