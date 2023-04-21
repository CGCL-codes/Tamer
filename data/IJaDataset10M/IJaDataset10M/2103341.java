package com.novell.ldapchai.exception;

/**
 * Indicates that the policy is not possible to fulfill.  For example, if the
 * policy's minimum length is larger then the maximum length
 * <p/>
 * The contents of the message will include a brief english discription of the
 * error, suitable for debugging.
 *
 * @author Jason D. Rivard
 */
public class ImpossiblePasswordPolicyException extends RuntimeException {

    /**
     * Enumeration of problems with a password policy
     */
    public enum ErrorEnum {

        MAX_LENGTH_GREATER_THEN_MINIMUM_LENGTH, MAX_LOWER_GREATER_THEN_MIN_LOWER, MAX_UPPER_GREATER_THEN_MIN_UPPER, MAX_NUMERIC_GREATER_THAN_MIN_NUMERIC, MAX_SPECIAL_GREATER_THAN_MIN_SPECIAL, MIN_LOWER_GREATER_THAN_MAX_LENGTH, MIN_NUMERIC_GREATER_THAN_MAX_LENGTH, MIN_SPECIAL_GREATER_THAN_MAX_LENGTH, MIN_UNIQUE_GREATER_THAN_MAX_LENGTH, MIN_UPPER_GREATER_THAN_MAX_LENGTH, REQUIRED_CHAR_NOT_ALLOWED, PASSWORD_TOO_COMPLEX_TO_GENERATE, UNEXPECTED_ERROR
    }

    private ErrorEnum error;

    public ImpossiblePasswordPolicyException(final ErrorEnum error) {
        super(error.toString());
        this.error = error;
    }

    public ErrorEnum getError() {
        return error;
    }
}
