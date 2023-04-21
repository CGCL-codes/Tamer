package org.dbunit.dataset;

import org.dbunit.DatabaseUnitException;

/**
 * Thrown to indicate that a problem occurred with a dataset.
 * 
 * @author Manuel Laflamme
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 793 $ $Date: 2008-08-17 07:35:43 -0400 (Sun, 17 Aug 2008) $
 * @since Feb 17, 2002
 */
public class DataSetException extends DatabaseUnitException {

    public DataSetException() {
    }

    public DataSetException(String msg) {
        super(msg);
    }

    public DataSetException(String msg, Throwable e) {
        super(msg, e);
    }

    public DataSetException(Throwable e) {
        super(e);
    }
}
