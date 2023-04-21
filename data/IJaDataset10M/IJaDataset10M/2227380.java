package org.paralit.isf.exceptions;

public class SearchEngineException extends ISFException {

    public static final long serialVersionUID = 1L;

    public SearchEngineException() {
        super();
    }

    public SearchEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchEngineException(String message) {
        super(message);
    }

    public SearchEngineException(Throwable cause) {
        super(cause);
    }
}
