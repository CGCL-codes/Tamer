package com.google.code.bing.search.client;

/**
 * The Class BingSearchException.
 */
public class BingSearchException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2392119987027760999L;

    /**
	 * Instantiates a new bing search exception.
	 */
    public BingSearchException() {
    }

    /**
	 * Instantiates a new bing search exception.
	 * 
	 * @param message the message
	 */
    public BingSearchException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new bing search exception.
	 * 
	 * @param cause the cause
	 */
    public BingSearchException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new bing search exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public BingSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
