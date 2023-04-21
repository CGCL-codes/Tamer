package ubadb.components.exceptions;

import ubadb.exceptions.UBADBException;

@SuppressWarnings("serial")
public class DBComponentException extends UBADBException {

    public DBComponentException() {
        super();
    }

    public DBComponentException(String arg0) {
        super(arg0);
    }

    public DBComponentException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DBComponentException(Throwable arg0) {
        super(arg0);
    }
}
