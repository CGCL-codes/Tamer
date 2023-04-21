package com.makotan.exception;

public class InvocationRuntimeException extends MRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public InvocationRuntimeException(String arg) {
        super(arg);
    }

    public InvocationRuntimeException(String arg, Throwable th) {
        super(arg, th);
    }

    public InvocationRuntimeException(Throwable th) {
        super(th);
    }
}
