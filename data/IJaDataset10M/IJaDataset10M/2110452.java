package com.peusoft.ptcollect.core.err;

/**
 * Exception for the case if the user name is wrong.
 * Error code is IErrorCodes.ERR_WRONG_USER_NAME.
 * @author Yauheni Prykhodzka
 * @version 1.0
 *
 */
public class PTExceptionWrongUser extends PTException {

    /**
     * Default constructor.
     */
    public PTExceptionWrongUser() {
        super(ErrorCode.ERR_WRONG_USER_NAME);
    }

    /**
     * Constructor.
     * @param message error message
     */
    public PTExceptionWrongUser(String message) {
        super(ErrorCode.ERR_WRONG_USER_NAME, message);
    }

    /**
     * Constructor.
     * @param cause caused exception
     */
    public PTExceptionWrongUser(Throwable cause) {
        super(ErrorCode.ERR_WRONG_USER_NAME, cause);
    }

    /**
     * Constructor.
     * @param message error message
     * @param cause caused exception
     */
    public PTExceptionWrongUser(String message, Throwable cause) {
        super(ErrorCode.ERR_WRONG_USER_NAME, message, cause);
    }
}
