package org.openware.job.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * To allow specific <code>Persist</code> objects to
 * bypass the default means of doing updates a developer
 * can implement this interface and set the raw update
 * for the desired <code>Persist</code> object.
 *
 * @author Vincent Sheffer
 * @version $Revision: 1.2 $ $Date: 2001/07/26 01:57:22 $
 */
public interface IRawUpdate extends Serializable {

    public int execute(Connection conn, TableRow shell) throws SQLException;
}
