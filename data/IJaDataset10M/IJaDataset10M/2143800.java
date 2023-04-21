package com.adam.framework.jdbc.exception;

/**
 * Exception thrown when an attempt to insert or update data
 * results in violation of an integrity constraint. Note that this
 * is not purely a relational concept; unique primary keys are
 * required by most database types.
 *
 * @author Rod Johnson
 */
public class DataIntegrityViolationException extends NonTransientDataAccessException {

    /**
	 * Constructor for DataIntegrityViolationException.
	 * @param msg the detail message
	 */
    public DataIntegrityViolationException(String msg) {
        super(msg);
    }

    /**
	 * Constructor for DataIntegrityViolationException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
    public DataIntegrityViolationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
