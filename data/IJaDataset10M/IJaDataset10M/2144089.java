package com.entelience.license;

import de.schlichtherle.license.LicenseContentException;

/**
* License validation exception
*/
public class ValidationException extends LicenseContentException {

    public ValidationException() {
        super(null);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Exception src) {
        super(null);
        initCause(src);
    }

    public ValidationException(String message, Exception src) {
        super(message);
        initCause(src);
    }

    public String getLocalizedMessage() {
        return ((Exception) this).getMessage();
    }
}
