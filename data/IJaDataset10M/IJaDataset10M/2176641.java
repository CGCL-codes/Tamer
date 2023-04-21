package com.jvito.exception;

/**
 * Indicates that a Key was not found.
 * 
 * @author Daniel Hakenjos
 * @version $Id: KeyException.java,v 1.2 2008/04/09 21:48:34 djhacker Exp $
 * 
 */
public class KeyException extends Exception {

    private static final long serialVersionUID = -5162737654958168294L;

    /**
	 * Creates a new KeyException with a message.
	 * 
	 * @param message
	 */
    public KeyException(String message) {
        super(message);
    }

    /**
	 * Creates a new KeyException with a message and a Throwable-Object.
	 * 
	 * @param message
	 * @param throwable
	 */
    public KeyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
