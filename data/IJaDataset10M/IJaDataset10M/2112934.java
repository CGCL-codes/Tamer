package rubbish.db.exception;

/**
 * rubbish-db�̗�O
 * 
 * @author $Author: winebarrel $
 * @version $Revision: 1.1 $
 */
public class RubbishDatabaseException extends RuntimeException {

    public RubbishDatabaseException(String message) {
        super(message);
    }

    public RubbishDatabaseException(String message, Exception cause) {
        super(message, cause);
    }

    public RubbishDatabaseException(Exception cause) {
        super(cause);
    }
}
