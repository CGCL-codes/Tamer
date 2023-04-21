package com.idna.gav.exceptions;

public class VoltSystemErrorException extends GavApplicationException {

    private static final long serialVersionUID = 1L;

    public VoltSystemErrorException() {
        super();
    }

    public VoltSystemErrorException(String gavErrorCode, String internalErrorMessage, Throwable t) {
        super(gavErrorCode, internalErrorMessage, t);
    }

    public VoltSystemErrorException(String gavErrorCode, String internalErrorMessage) {
        super(gavErrorCode, internalErrorMessage);
    }

    public VoltSystemErrorException(String internalErrorMessage, Throwable t) {
        super(internalErrorMessage, t);
    }

    public VoltSystemErrorException(String internalErrorMessage) {
        super(internalErrorMessage);
    }

    public VoltSystemErrorException(Throwable cause) {
        super(cause);
    }
}
