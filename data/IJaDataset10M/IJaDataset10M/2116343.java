package org.jazzteam.jpatterns.patterns.mediator.exceptions;

/**
 * $Author:: $<br/>
 * $Rev:: $<br/>
 * $Date:: $<br/>
 */
public class CannotRegisterClientException extends Exception {

    /**
	 * The default public constructor
	 */
    public CannotRegisterClientException() {
        super();
    }

    /**
	 * One of the public constructor
	 * 
	 * @param message
	 *            the message param
	 */
    public CannotRegisterClientException(final String message) {
        super(message);
    }

    /**
	 * One of the public constructors
	 * 
	 * @param message
	 *            message param
	 * @param cause
	 *            param
	 */
    public CannotRegisterClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
	 * One of the forms of the public construtor
	 * 
	 * @param cause
	 *            the throwable param
	 */
    public CannotRegisterClientException(final Throwable cause) {
        super(cause);
    }
}
