package com.linglingqi.mathematics.numbers;

/**
 * Indicates an attempt was made to divide by zero.
 **/
public final class DivisionByZeroException extends NumberException {

    /**
     * Embedded Copyright Notice.
     **/
    public static final java.lang.String EMBEDDED_COPYRIGHT = "Copyright (C) 2008, Emory Merryman, ree9opdz@gmail.com";

    /**
     * Hide the no-arg constructor.
     **/
    private DivisionByZeroException() {
        super();
    }

    /**
     * Throws a DivisionByZeroException.
     *
     * @throws DivisionByZeroException always
     **/
    public static final void error() throws DivisionByZeroException {
        final DivisionByZeroException error = new DivisionByZeroException();
        throw (error);
    }
}
