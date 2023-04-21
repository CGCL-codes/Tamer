package com.vangent.hieos.xutil.exception;

/**
 *
 * @author thumbe
 */
public class NoSubmissionSetException extends MetadataException {

    /**
     *
     * @param msg
     */
    public NoSubmissionSetException(String msg) {
        super(msg);
    }

    /**
     *
     * @param msg
     * @param cause
     */
    public NoSubmissionSetException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
