package com.makotan.exception;

public class FileNotFoundRuntimeException extends MRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public FileNotFoundRuntimeException(String arg) {
        super(arg);
    }

    public FileNotFoundRuntimeException(String arg, Throwable th) {
        super(arg, th);
    }

    public FileNotFoundRuntimeException(Throwable th) {
        super(th);
    }
}
