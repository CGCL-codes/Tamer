package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.WBSAXException;

/**
 * The exception thrown by WBDOM classes.
 * <p>
 * Initially it seemed attractive to make this exception extend 
 * {@link WBSAXException} so that we could avoid wrapping exceptions as they
 * passed up the stack from WBSAX to WBDOM. Unfortunately, this doesn't work
 * because in order to avoid the wrapping, the inverse is actually required 
 * (i.e. the WBSAX exception would have to extend the WBDOM exception), which
 * is clearly bogus. Seems like there is some underlying problem with Java
 * in this regard but I have no more time to think about it.
 */
public class WBDOMException extends Exception {

    /**
     * Create an instance of this class with the message specified. 
     * 
     * @param message the exception message to use.
     */
    public WBDOMException(String message) {
        super(message);
    }

    /**
     * Create an instance of this class with the cause specified. 
     * 
     * @param cause the exception which caused this exception.
     */
    public WBDOMException(Throwable cause) {
        super(cause);
    }

    /**
     * Create an instance of this class with the message and cause specified. 
     * 
     * @param message the exception message to use.
     * @param cause the exception which caused this exception.
     */
    public WBDOMException(String message, Throwable cause) {
        super(message, cause);
    }
}
