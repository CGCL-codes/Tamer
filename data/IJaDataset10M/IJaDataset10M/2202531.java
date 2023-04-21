package org.dcm4chex.service;

/**
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 1090 $ $Date: 2004-04-22 08:45:01 -0400 (Thu, 22 Apr 2004) $
 * @since 20.04.2004
 */
public class NoPresContextException extends Exception {

    /**
     * 
     */
    public NoPresContextException() {
        super();
    }

    /**
     * @param message
     */
    public NoPresContextException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NoPresContextException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NoPresContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
