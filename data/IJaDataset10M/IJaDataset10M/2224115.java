package au.edu.diasb.chico.mvc;

/**
 * This exception is thrown when a controller discovers that a request parameter
 * has an invalid value.
 * 
 * @author scrawley
 */
public class InvalidRequestParameterException extends RequestParameterException {

    private static final long serialVersionUID = -3681876718706379141L;

    public InvalidRequestParameterException(String param) {
        super(param, "Invalid request parameter: '" + param + "'");
    }

    public InvalidRequestParameterException(String param, String message) {
        super(param, message);
    }

    public InvalidRequestParameterException(int status, String param, String message) {
        super(status, param, message);
    }

    public InvalidRequestParameterException(int status, String param, String message, Throwable ex) {
        super(status, param, message, ex);
    }

    public InvalidRequestParameterException(String param, String message, Throwable ex) {
        super(param, message, ex);
    }
}
