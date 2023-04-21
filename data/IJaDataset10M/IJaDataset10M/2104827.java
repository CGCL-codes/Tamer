package org.ikross.twitter.exception;

public class InternalServerException extends ConnectorException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2368470699488975031L;

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(Exception except) {
        super(except);
    }
}
