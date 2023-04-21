package com.makotan.exception;

public class FileSystemRuntimeException extends MRuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public FileSystemRuntimeException(String arg) {
        super(arg);
    }

    public FileSystemRuntimeException(String arg, Throwable th) {
        super(arg, th);
    }

    public FileSystemRuntimeException(Throwable th) {
        super(th);
    }
}
