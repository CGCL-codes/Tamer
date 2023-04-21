package ua.gradsoft.misc.jsoo;

/**
 *Handler for exceptions, which are throwed during JPE processing.
 * @author Ruslan Shevchenko
 */
public class JSOOProcessingException extends Exception {

    public JSOOProcessingException(String message) {
        super(message);
    }

    public JSOOProcessingException(String message, Exception ex) {
        super(message, ex);
    }
}
