package com.hp.hpl.jena.shared;

import java.io.IOException;

/**
    A wrapped IO exception.
    @author hedgehog
*/
public class WrappedIOException extends JenaException {

    public WrappedIOException(IOException cause) {
        super(cause);
    }

    public WrappedIOException(String message, IOException cause) {
        super(message, cause);
    }
}
