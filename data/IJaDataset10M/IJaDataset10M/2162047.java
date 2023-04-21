package com.continuent.tungsten.router.adaptor;

public class RouterAdaptorException extends Exception {

    private static final long serialVersionUID = 8339994478408859274L;

    public RouterAdaptorException(String reason) {
        super(reason);
    }

    public RouterAdaptorException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
