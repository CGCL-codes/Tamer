package net.sf.jgmail;

/**
 * @author todd
 */
public class LoginException extends Exception {

    private static final long serialVersionUID = 1;

    /**
    * @param message
    */
    public LoginException(String message, String rawResponse) {
        super(message + ".  Raw Response is: \n" + rawResponse);
    }

    /**
    * @param cause
    */
    public LoginException(String rawResponse, Throwable cause) {
        super("Raw Response is: \n" + rawResponse, cause);
    }

    /**
    * @param message
    * @param cause
    */
    public LoginException(String message, String rawResponse, Throwable cause) {
        super(message + ".  Raw Response is: \n" + rawResponse, cause);
    }
}
