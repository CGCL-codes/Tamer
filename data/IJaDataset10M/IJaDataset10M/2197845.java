package com.common.exception;

public class PersistenceException extends InternalApplicationException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4208969157038932607L;

    public PersistenceException() {
        super();
    }

    public PersistenceException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public PersistenceException(String arg0) {
        super(arg0);
    }

    public PersistenceException(Throwable arg0) {
        super(arg0);
    }
}
