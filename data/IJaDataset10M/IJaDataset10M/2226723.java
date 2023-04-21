package com.makotan.exception;

public class UnsupportedEncodingRuntimeException extends MRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public UnsupportedEncodingRuntimeException(String arg) {
        super(arg);
    }

    public UnsupportedEncodingRuntimeException(String arg, Throwable th) {
        super(arg, th);
    }

    public UnsupportedEncodingRuntimeException(Throwable th) {
        super(th);
    }
}
