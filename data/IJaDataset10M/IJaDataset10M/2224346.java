package com.googlecode.jazure.sdk.job.exception;

import com.googlecode.jazure.sdk.core.JAzureException;

public class JobNotRunningException extends JAzureException {

    private static final long serialVersionUID = -6067794912159539338L;

    public JobNotRunningException() {
        super();
    }

    public JobNotRunningException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobNotRunningException(String message) {
        super(message);
    }

    public JobNotRunningException(Throwable cause) {
        super(cause);
    }
}
