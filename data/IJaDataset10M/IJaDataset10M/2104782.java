package prisms.arch;

/**
 * A PrismsDetailException is a parameterized exception that allows programs to pass additional
 * information to the client than merely the exception's message when code fails
 */
public class PrismsDetailException extends RuntimeException {

    private final org.json.simple.JSONObject theParams;

    /**
	 * @param msg The message for the exception
	 * @param params The parameters that should be passed to the client along with this exception's
	 *        message
	 */
    public PrismsDetailException(String msg, Object... params) {
        super(msg);
        theParams = prisms.util.PrismsUtils.rEventProps(params);
    }

    /**
	 * @param msg The message for the exception
	 * @param cause The cause of the exception
	 * @param params The parameters that should be passed to the client along with this exception's
	 *        message
	 */
    public PrismsDetailException(String msg, Throwable cause, Object... params) {
        super(msg, cause);
        theParams = prisms.util.PrismsUtils.rEventProps(params);
    }

    /**
	 * @return The parameters that are associated with this error
	 */
    public org.json.simple.JSONObject getParams() {
        return theParams;
    }
}
