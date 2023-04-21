package com.google.code.bing.webservices.client.exception;

/**
 * @author nmukhtar
 *
 */
public class BingMapsSearchServiceClientException extends BingMapsWebServicesClientException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8093177324428029624L;

    /**
	 * 
	 */
    public BingMapsSearchServiceClientException() {
    }

    /**
	 * @param message
	 */
    public BingMapsSearchServiceClientException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public BingMapsSearchServiceClientException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public BingMapsSearchServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param authenticationResultCode
	 * @param copyright
	 * @param faultReason
	 * @param statusCode
	 * @param traceId
	 */
    public BingMapsSearchServiceClientException(String message, Throwable cause, String authenticationResultCode, String copyright, String faultReason, String statusCode, String traceId) {
        super(message, cause, authenticationResultCode, copyright, faultReason, statusCode, traceId);
    }
}
