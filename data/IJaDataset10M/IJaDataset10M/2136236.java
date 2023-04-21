package de.herberlin.wwwutil.httperror;

/**
 *
 * @author Hans Joachim Herbertz
 * @created 30.01.2003
 */
public class BadRequest_400 extends HttpError {

    /**
	 * Constructor for BadRequest_400.
	 */
    public BadRequest_400() {
        super();
    }

    /**
	 * Constructor for BadRequest_400.
	 * @param message
	 */
    public BadRequest_400(String message) {
        super(message);
    }

    /**
	 * Constructor for BadRequest_400.
	 * @param message
	 * @param cause
	 */
    public BadRequest_400(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor for BadRequest_400.
	 * @param cause
	 */
    public BadRequest_400(Throwable cause) {
        super(cause);
    }

    protected String getReason() {
        return "400 Bad Request";
    }

    protected String getMoreHeaders() {
        return null;
    }
}
