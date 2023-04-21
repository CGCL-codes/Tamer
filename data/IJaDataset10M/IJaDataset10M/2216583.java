package gui.exceptions;

/**
 * Exception relating to queries
 */
public class QueryException extends Exception {

    private static final long serialVersionUID = -706164518724103468L;

    /**
	 * Default constructor
	 */
    public QueryException() {
        super();
    }

    /**
	 * Construct exception with message
	 * @param string exception message
	 */
    public QueryException(final String string) {
        super(string);
    }
}
