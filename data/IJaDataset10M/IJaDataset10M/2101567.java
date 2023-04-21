package org.jdeluxe.exceptions;

/**
 * The Class NonSingleResultException.
 */
public class NonSingleResultException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new non single result exception.
	 * 
	 * @param msg the msg
	 */
    public NonSingleResultException(String msg) {
        super(msg);
    }
}
