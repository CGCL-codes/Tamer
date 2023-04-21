package storage.sql;

/**
 * The SimpleDB version of the java.sql.SQLException class.
 * Because JDBC is a language-independent standard,
 * it builds the exception-handling mechanism directly into
 * the class.  SimpleDB chooses instead to take advantage of 
 * the Java exception-handling mechanism,
 * which is why the constructor differs from the standard.
 * @author Edward Sciore
 */
@SuppressWarnings("serial")
public class SQLException extends Exception {

    private String message;

    /**
	 * Create an SQL exception based on the specified
	 * Java exception.
	 * @param cause the Java exception
	 */
    public SQLException(Exception cause) {
    }

    /**
	 * Returns a message describing the cause of the exception.
	 * @see java.lang.Throwable#getMessage()
	 */
    public String getMessage() {
        return message;
    }
}
