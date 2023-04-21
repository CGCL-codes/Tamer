package sqs.exception;

/**
 * @author kjleng
 *
 */
public class DataAccessException extends Exception {

    public DataAccessException() {
        super("Error in accessing DB!");
    }

    public DataAccessException(String msg) {
        super(msg);
    }
}
