package com.jy.bookshop.constants;

public class ErrorCode {

    private int errorCode;

    /**
	 * @return the errorCode
	 */
    public int getErrorCode() {
        return errorCode;
    }

    /**
	 * @param errorCode the errorCode to set
	 */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public static final int ERROR_NO_ERROR = 0;

    public static final int ERROR_NO_REASON = 1;

    public static final int ERROR_PASSWORD_WRONG = 2;

    public static final int ERROR_NO_USER = 5;

    public static final int PARAM_IS_NULL = 3;

    public static final int NO_USER_ID = 4;
}
