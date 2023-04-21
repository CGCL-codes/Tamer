package org.t2framework.samples.lucy.exception;

public class SQLRuntimeException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public SQLRuntimeException(Throwable e) {
        super(e);
    }
}
