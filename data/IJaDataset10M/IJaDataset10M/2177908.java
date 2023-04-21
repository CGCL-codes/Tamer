package com.meetup.memcached;

import java.io.*;

/**
 * Bridge class to provide nested Exceptions with IOException which has
 * constructors that don't take Throwables.
 * 
 * @author <a href="mailto:burton@rojo.com">Kevin Burton</a>
 * @version 1.2
 */
public class NestedIOException extends IOException {

    /**
     * Create a new <code>NestedIOException</code> instance.
     * @param cause object of type throwable
     */
    public NestedIOException(Throwable cause) {
        super(cause.getMessage());
        super.initCause(cause);
    }

    public NestedIOException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
