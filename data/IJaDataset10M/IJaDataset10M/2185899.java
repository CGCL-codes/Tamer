package org.cilogon.d2.util;

import edu.uiuc.ncsa.security.core.exceptions.GeneralException;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 3/1/12 at  1:25 PM
 */
public class CILogonException extends GeneralException {

    public CILogonException() {
    }

    public CILogonException(Throwable cause) {
        super(cause);
    }

    public CILogonException(String message) {
        super(message);
    }

    public CILogonException(String message, Throwable cause) {
        super(message, cause);
    }
}
