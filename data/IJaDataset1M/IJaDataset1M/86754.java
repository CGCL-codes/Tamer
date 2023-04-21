package org.hyperimage.connector.exception;

/**
 * Wraps an authorisation exception (usually ClientTransportException) in a HyperImage Web Service Exception.
 * 
 * @author Heinz-Günter Kuper
 */
public class HIWSAuthException extends Exception {

    /**
	 * 03-09-2008
	 */
    private static final long serialVersionUID = 7250214282250670148L;

    public HIWSAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
