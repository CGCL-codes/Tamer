package com.bing.maps.rest.services;

/**
 * The Class NotFoundException.
 */
public class NotFoundException extends BingMapsException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2392119987027760999L;

    /**
	 * Instantiates a new not found exception.
	 */
    public NotFoundException() {
    }

    /**
	 * Instantiates a new not found exception.
	 * 
	 * @param message the message
	 */
    public NotFoundException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new not found exception.
	 * 
	 * @param cause the cause
	 */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new not found exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
