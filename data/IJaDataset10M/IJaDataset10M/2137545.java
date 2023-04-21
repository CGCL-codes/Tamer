package com.patientis.ejb.reference;

/**
 * @author gcaulton
 *
 */
public class ReferenceKeyDoesNotMatchException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ReferenceKeyDoesNotMatchException() {
    }

    /**
	 * 
	 * @param msg
	 */
    public ReferenceKeyDoesNotMatchException(String msg) {
        super(msg);
    }
}
