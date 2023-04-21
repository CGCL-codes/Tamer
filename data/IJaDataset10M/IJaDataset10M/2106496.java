package com.google.gwt.eclipse.core.uibinder;

/**
 * Generic exception for UiBinder problems.
 */
@SuppressWarnings("serial")
public class UiBinderException extends Exception {

    public UiBinderException(String message) {
        super(message);
    }

    public UiBinderException(String message, Throwable cause) {
        super(message, cause);
    }

    public UiBinderException(Throwable cause) {
        super(cause);
    }
}
