package org.jcvi.vics.compute.service.common.zip;

import org.jcvi.vics.compute.engine.service.ServiceException;

/**
 * Represents the any exceptions encountered during zip service execution
 *
 * @author Tareq Nabeel
 */
public class ZipException extends ServiceException {

    /**
     * Construct a ZipException with a descriptive String
     *
     * @param msg The string that describes the error
     */
    public ZipException(String msg) {
        super(msg);
    }

    /**
     * Construct a ZipException to wrap another exception.
     *
     * @param e The exception to be wrapped.
     */
    public ZipException(Throwable e) {
        super(e);
    }

    /**
     * Construct a ZipException to wrap another exception.
     *
     * @param e The exception to be wrapped.
     */
    public ZipException(String msg, Throwable e) {
        super(msg, e);
    }

    /**
     * Construct a ZipException with a descriptive String
     *
     * @param msg       The string that describes the error
     * @param errorCode more description on the error for possible special handling
     */
    public ZipException(String msg, int errorCode) {
        super(msg);
        setErrorCode(errorCode);
    }

    /**
     * Construct a ZipException to wrap another exception.
     *
     * @param e         The exception to be wrapped.
     * @param errorCode more description on the error for possible special handling
     */
    public ZipException(Throwable e, int errorCode) {
        super(e);
        setErrorCode(errorCode);
    }

    /**
     * Construct a ZipException to wrap another exception.
     *
     * @param e         The exception to be wrapped.
     * @param errorCode more description on the error for possible special handling
     */
    public ZipException(String msg, Throwable e, int errorCode) {
        super(msg, e);
        setErrorCode(errorCode);
    }
}
