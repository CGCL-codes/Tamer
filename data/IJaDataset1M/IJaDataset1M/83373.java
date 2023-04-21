package com.vangent.hieos.xutil.exception;

/**
 *
 * @author thumbe
 */
public class XdsIOException extends XdsException {

    /**
     *
     * @param msg
     */
    public XdsIOException(String msg) {
        super(msg);
    }

    /**
     *
     * @param msg
     * @param cause
     */
    public XdsIOException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
