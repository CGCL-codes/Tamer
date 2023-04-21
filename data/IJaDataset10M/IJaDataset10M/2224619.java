package com.osgix.common;

public abstract class AbsSysExecption extends Exception implements ISysExecption {

    private String sysErrorCode;

    private String errorMessage;

    public String getMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSysErrorCode() {
        return this.sysErrorCode;
    }

    public void setSysErrorCode(String sysErrorCode) {
        this.sysErrorCode = sysErrorCode;
    }

    public AbsSysExecption() {
        super();
    }

    public AbsSysExecption(String message) {
        super(message);
    }

    public AbsSysExecption(Throwable cause) {
        super(cause);
    }

    public AbsSysExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
