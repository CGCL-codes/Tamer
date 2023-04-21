package org.expasy.jpl.matching.exception;

import org.expasy.jpl.utils.exceptions.JPLException;

public class JPLSpectralMatcherException extends JPLException {

    private static final long serialVersionUID = 1L;

    public JPLSpectralMatcherException() {
    }

    public JPLSpectralMatcherException(String message) {
        super(message);
    }

    public JPLSpectralMatcherException(Throwable cause) {
        super(cause);
    }

    public JPLSpectralMatcherException(String message, Throwable cause) {
        super(message, cause);
    }
}
