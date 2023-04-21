package org.dbunit.dataset.sqlloader;

import org.dbunit.DatabaseUnitRuntimeException;

/**
 * @author Stephan Strittmatter (stritti AT users.sourceforge.net)
 * @author Last changed by: $Author: gommma $
 * @version $Revision: 817 $ $Date: 2008-09-29 16:23:35 -0400 (Mon, 29 Sep 2008) $
 * @since 2.4.0
 */
public class SqlLoaderControlParserException extends DatabaseUnitRuntimeException {

    /**
     * The Constructor.
     * 
     * @param message the message
     */
    public SqlLoaderControlParserException(String message) {
        super(message);
    }
}
