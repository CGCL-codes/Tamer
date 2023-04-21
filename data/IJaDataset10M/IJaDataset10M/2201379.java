package org.smslib.v3;

/**
 * Exception class specific to timeout errors.
 */
public class TimeoutException extends SMSLibException {

    private static final long serialVersionUID = 1L;

    public TimeoutException(String errorMessage) {
        super(errorMessage);
    }
}
