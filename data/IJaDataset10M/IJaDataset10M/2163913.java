package com.jvantage.ce.acl;

import org.apache.commons.lang.exception.NestableException;

/**
 *
 * @author  bclay
 */
public class ACLException extends NestableException implements java.io.Serializable {

    /**
     * Creates a new instance of <code>ACLException</code> without detail message.
     */
    public ACLException() {
    }

    /**
     * Constructs an instance of <code>ACLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ACLException(String msg) {
        super(msg);
    }

    public ACLException(Throwable e) {
        super(e);
    }

    /**
     * Constructs an instance of <code>ACLException</code> with the specified detail message.
     *
     * @param msg the detail message.
     * @param nested throwable.
     */
    public ACLException(String msg, Throwable nestedException) {
        super(msg, nestedException);
    }
}
