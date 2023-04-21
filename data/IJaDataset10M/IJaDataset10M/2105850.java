package com.siberhus.org.apache.commons.validator.routines.checkdigit;

/**
 * Check Digit calculation/validation error.
 *
 * @version $Revision: 493905 $ $Date: 2007-01-07 21:11:38 -0500 (Sun, 07 Jan 2007) $
 * @since Validator 1.4
 */
public class CheckDigitException extends Exception {

    /**
     * Construct an Exception with no message.
     */
    public CheckDigitException() {
    }

    /**
     * Construct an Exception with a message.
     *
     * @param msg The error message.
     */
    public CheckDigitException(String msg) {
        super(msg);
    }

    /**
     * Construct an Exception with a message and
     * the underlying cause.
     *
     * @param msg The error message.
     * @param cause The underlying cause of the error
     */
    public CheckDigitException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
