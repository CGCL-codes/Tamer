package org.kablink.teaming.exception;

import org.kablink.teaming.util.NLT;

/**
 * @author Jong Kim
 *
 */
public abstract class CheckedCodedException extends Exception implements ErrorCodeSupport {

    private String errorCode;

    private Object[] errorArgs;

    public CheckedCodedException(String errorCode) {
        super();
        setErrorCode(errorCode);
    }

    public CheckedCodedException(String errorCode, Object[] errorArgs) {
        super();
        setErrorCode(errorCode);
        setErrorArgs(errorArgs);
    }

    public CheckedCodedException(String errorCode, Object[] errorArgs, String message) {
        super(message);
        setErrorCode(errorCode);
        setErrorArgs(errorArgs);
    }

    public CheckedCodedException(String errorCode, Object[] errorArgs, String message, Throwable cause) {
        super(message, cause);
        setErrorCode(errorCode);
        setErrorArgs(errorArgs);
    }

    public CheckedCodedException(String errorCode, Object[] errorArgs, Throwable cause) {
        super(cause);
        setErrorCode(errorCode);
        setErrorArgs(errorArgs);
    }

    public String getLocalizedMessage() {
        try {
            return NLT.get(getErrorCode(), getErrorArgs());
        } catch (Exception e) {
            return super.getLocalizedMessage();
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getErrorArgs() {
        return errorArgs;
    }

    public void setErrorArgs(Object[] errorArgs) {
        this.errorArgs = errorArgs;
    }

    protected void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
