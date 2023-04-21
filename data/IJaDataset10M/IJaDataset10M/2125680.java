package com.newisys.schemaprinter;

import java.io.IOException;

/**
 * RuntimeException wrapper for IOExceptions used to allow IOExceptions to
 * propagate as unchecked exceptions through Visitor interfaces.
 * 
 * @author Trevor Robinson
 */
public final class WrappedIOException extends RuntimeException {

    private static final long serialVersionUID = 3258415049248027448L;

    public WrappedIOException(IOException cause) {
        super(cause);
    }

    public IOException getIOException() {
        return (IOException) getCause();
    }
}
