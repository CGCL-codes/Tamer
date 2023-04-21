package de.ios.kontor.utils;

import de.ios.framework.basic.KontorException;

/**
 * This is the common NotPrintableException. It means that something can't be printed.
 */
public class NotPrintableException extends KontorException {

    public NotPrintableException(String msg) {
        super(msg);
    }
}

;
