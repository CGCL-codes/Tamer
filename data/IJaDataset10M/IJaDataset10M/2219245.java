package pub.db;

import pub.servlets.ConstraintException;

/**
 * Represents an exception that's thrown then a database constraint
 * has been violated.
 */
public class DatabaseConstraintException extends ConstraintException {

    public DatabaseConstraintException() {
    }

    public DatabaseConstraintException(String reason) {
        super("/jsp/error/DatabaseConstraintException.jsp", reason);
    }

    public DatabaseConstraintException(String jsp_url, String reason) {
        super(jsp_url, reason);
    }
}
