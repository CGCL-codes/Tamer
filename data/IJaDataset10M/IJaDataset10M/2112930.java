package org.lightrpc;

/**
 * This class represents a error that is thrown between a client and a server.
 * It contains an error message and may contain recursive references.
 * 
 * @author miku
 * 
 */
public class RpcError {

    private String errorName;

    private String errorMessage;

    private RpcError cause;

    /**
	 * Constructor used to create
	 * errors, that can be serialized.
	 * @param t
	 */
    public RpcError(Throwable t) {
        errorName = t.getClass().getSimpleName();
        errorMessage = t.getMessage();
        if (t.getCause() != null) {
            cause = new RpcError(t.getCause());
        }
    }

    /**
	 * Default constructor.
	 * Needed for some serialization engines.
	 */
    public RpcError() {
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RpcError getCause() {
        return cause;
    }

    public void setCause(RpcError cause) {
        this.cause = cause;
    }
}
