package org.ourgrid.common.interfaces;

import org.ourgrid.common.interfaces.to.RequestSpec;

/**
 * Represents an error that occurs inside a WorkerProvider when trying to
 * respond a client request (i.e. a request for workers).
 */
public class WorkerProviderError {

    private Throwable errorCause;

    /**
	 * The <code>RequestSpec</code> to describe which request was associated
	 * to the error (if any).
	 */
    private RequestSpec requestSpec;

    public WorkerProviderError(Throwable cause, RequestSpec spec) {
        super();
        errorCause = cause;
        requestSpec = spec;
    }

    public Throwable getErrorCause() {
        return errorCause;
    }

    public RequestSpec getRequestSpec() {
        return requestSpec;
    }
}
