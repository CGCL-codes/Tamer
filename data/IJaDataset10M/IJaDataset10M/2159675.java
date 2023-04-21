package com.patientis.ejb.reference;

/**
 * ModelNotFoundException is thrown when a model is retreived with an invalid id
 *
 * <br/>Design Patterns: <a href="/functionality/rm/1000047.html">Exceptions</a>
 * <br/>
 */
public class MultipleRefTreesFoundException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public MultipleRefTreesFoundException() {
        super();
    }

    /**
	 * Default constructor
	 * 
	 * @param message
	 * @param cause
	 */
    public MultipleRefTreesFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Default constructor
	 * 
	 * @param message
	 */
    public MultipleRefTreesFoundException(String message) {
        super(message);
    }

    /**
	 * Default constructor
	 * 
	 * @param cause
	 */
    public MultipleRefTreesFoundException(Throwable cause) {
        super(cause);
    }
}
