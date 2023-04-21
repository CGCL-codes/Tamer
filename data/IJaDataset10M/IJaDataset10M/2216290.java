package com.vangent.hieos.xutil.exception;

/**
 *
 * @author Bernie Thuman
 */
public class SOAPFaultException extends Exception {

    /**
     *
     * @param msg
     */
    public SOAPFaultException(String msg) {
        super(msg);
    }

    /**
     *
     * @param msg
     * @param cause
     */
    public SOAPFaultException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
