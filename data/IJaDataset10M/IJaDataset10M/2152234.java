package org.dbunit.operation;

import org.dbunit.DatabaseUnitException;

/**
 * @author Manuel Laflamme
 * @version $Revision: 676 $
 * @since Feb 21, 2002
 */
public class ExclusiveTransactionException extends DatabaseUnitException {

    private static final long serialVersionUID = 1L;

    public ExclusiveTransactionException() {
    }

    public ExclusiveTransactionException(String msg) {
        super(msg);
    }

    public ExclusiveTransactionException(String msg, Throwable e) {
        super(msg, e);
    }

    public ExclusiveTransactionException(Throwable e) {
        super(e);
    }
}
