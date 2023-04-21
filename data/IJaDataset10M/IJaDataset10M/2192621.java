package dbgate.ermanagement.exceptions;

import dbgate.BaseException;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Mar 26, 2011
 * Time: 12:43:17 PM
 */
public class PersistException extends BaseException {

    public PersistException() {
    }

    public PersistException(String s) {
        super(s);
    }

    public PersistException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PersistException(Throwable throwable) {
        super(throwable);
    }
}
