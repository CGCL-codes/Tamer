package com.jdbwc.exceptions;

public class IntegrityConstraintViolationException extends DataException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public IntegrityConstraintViolationException() {
        super();
    }

    public IntegrityConstraintViolationException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public IntegrityConstraintViolationException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public IntegrityConstraintViolationException(String reason) {
        super(reason);
    }
}
