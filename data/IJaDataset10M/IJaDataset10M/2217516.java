package com.escape.proxy;

/**
 * Proxy for Java Beans IntrospectionException.
 * @author escape-llc
 *
 */
public class IntrospectionException extends Exception {

    private static final long serialVersionUID = 1L;

    public IntrospectionException(String mess) {
        super(mess);
    }
}
