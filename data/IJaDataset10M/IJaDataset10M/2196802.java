package wotlas.libs.persistence;

/**
 * Exception when a problem occurs during persistence operation.
 * @author Hari
 */
public class PersistenceException extends Exception {

    /**
    * Construct a persistence exception from another exception.
    * @param exception    the root exception.
    **/
    protected PersistenceException(Exception exception) {
        super(exception.getMessage());
    }
}
