package com.ecs.etrade.bo.accounting;

/**
 * @author Alok Ranjan
 *
 */
public class AccountingException extends Exception {

    private static final long serialVersionUID = 3119120920769104770L;

    private String errorCode;

    private String exceptionID;

    private String message;

    public AccountingException(final String message, final String errorCode, final String exceptionID) {
        super();
        this.message = message;
        this.errorCode = errorCode;
        this.exceptionID = exceptionID;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getExceptionID() {
        return exceptionID;
    }

    public void setExceptionID(String exceptionID) {
        this.exceptionID = exceptionID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
