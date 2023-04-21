package com.linguamathematica.oa4j;

import static java.lang.String.format;

/**
 * Thrown when the service responds with an error or when the response was OK but its content is not valid.
 */
public class ResponseException extends RuntimeException {

    /** The status code. */
    private final int statusCode;

    /** The reason. */
    private final String reason;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7015869450117727041L;

    /**
	 * Instantiates a new ResponseException.
	 * 
	 * @param message
	 *            an explanation of the cause of the error
	 * @param statusCode
	 *            the HTTP status code
	 * @param reason
	 *            the HTTP reason phrase
	 */
    public ResponseException(final String message, final int statusCode, final String reason) {
        this(message, statusCode, reason, null);
    }

    /**
	 * Instantiates a new response exception.
	 * 
	 * @param message
	 *            an explanation of the cause of the error
	 * @param statusCode
	 *            the HTTP status code
	 * @param reason
	 *            the HTTP reason phrase
	 * @param cause
	 *            the cause
	 */
    public ResponseException(final String message, final int statusCode, final String reason, final Exception cause) {
        super(format("%s;  status code: [%s], reason: [%s]", message, statusCode, reason), cause);
        this.statusCode = statusCode;
        this.reason = reason;
    }

    /**
	 * Gets the reason.
	 * 
	 * @return the HTTP reason phrase
	 */
    public String getReason() {
        return reason;
    }

    /**
	 * Gets the the HTTP status code.
	 * 
	 * @return the status code
	 */
    public int getStatusCode() {
        return statusCode;
    }
}
