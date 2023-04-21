package jeeobserver;

/**
 * The Class DatabaseException.
 *
 * @author Luca Mingardi
 * @version 3.1
 */
public final class DatabaseException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 31L;

    /**
	 * Instantiates a new database exception.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 */
    protected DatabaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
	 * Instantiates a new database exception.
	 *
	 * @param arg0 the arg0
	 */
    protected DatabaseException(String arg0) {
        super(arg0);
    }

    /**
	 * Instantiates a new database exception.
	 *
	 * @param arg0 the arg0
	 */
    protected DatabaseException(Throwable arg0) {
        super(arg0);
    }
}
