package com.sitescape.team.module.ic;

public class ICException extends Exception {

    /**
	 * There was exception, look at <code>cause</code>.
	 */
    public static final int RPC_EXCEPTION = 0;

    /**
	 * There was error returned by IC, look at <code>errorCode</code>.
	 */
    public static final int RPC_ERROR = 1;

    private int type = RPC_EXCEPTION;

    private Integer errorCode = null;

    public ICException(Integer errorCode) {
        super(Integer.toString(errorCode));
        this.errorCode = errorCode;
        this.type = RPC_ERROR;
    }

    public ICException(Throwable cause) {
        super(cause);
        this.type = RPC_EXCEPTION;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public int getType() {
        return type;
    }
}
