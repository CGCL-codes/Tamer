package au.edu.diasb.emmet.access;

/**
 * This exception is thrown when a request needs to be confirmed with
 * a 'confirmed' parameter.
 * 
 * @author scrawley
 */
public class RequiresCaptchaException extends AccessPreconditionException {

    private static final long serialVersionUID = -7845212291467929307L;

    public RequiresCaptchaException(String message) {
        super(message);
    }
}
