package org.riverock.dbrevision.exception;

/**
 * User: SergeMaslyukov
 * Date: 25.02.2009
 * Time: 0:20:16
 * $Id$
 */
public class TableNotFoundException extends DbRevisionException {

    public TableNotFoundException() {
    }

    public TableNotFoundException(String s) {
        super(s);
    }

    public TableNotFoundException(Throwable cause) {
        super(cause);
    }

    public TableNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }
}
