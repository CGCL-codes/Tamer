package net.sf.fb4j.client;

/**
 * Wrapper for IOExceptions thrown when error communicating with Facebook REST
 * Server.
 * 
 * @author Gino Miceli
 */
public class FacebookIOException extends FacebookClientException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "Error communicating with Facebook REST Server.";

    public FacebookIOException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
