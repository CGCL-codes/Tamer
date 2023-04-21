package com.acciente.induction.util;

/**
 * Internal.
 *
 * @created Mar 20, 2008
 *
 * @author Adinath Raveendra Raj
 */
public class ConstructorNotFoundException extends Exception {

    public ConstructorNotFoundException(String sMessage) {
        super(sMessage);
    }

    public ConstructorNotFoundException(String sMessage, Throwable oCause) {
        super(sMessage, oCause);
    }
}
