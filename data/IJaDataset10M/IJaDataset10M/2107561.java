package com.patientis.framework.exceptions;

/**
 * One line class description
 *
 * 
 * 
 */
public class ReferenceMissingException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Reference missing
	 * 
	 * @param msg
	 */
    public ReferenceMissingException(String msg) {
        super(msg);
    }
}
