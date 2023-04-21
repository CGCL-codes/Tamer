package com.googlecode.batchfb.err;

/**
 * Root of the exception hierarchy for BatchFB.  A raw FacebookException will
 * be thrown when a more specific exception type cannot be determined.
 * 
 * @author Jeff Schnitzer
 */
public class FacebookException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Make GWT happy */
    FacebookException() {
    }

    /**
	 */
    public FacebookException(String message) {
        super(message);
    }

    /**
	 */
    public FacebookException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 */
    public FacebookException(Throwable cause) {
        super(cause);
    }
}
