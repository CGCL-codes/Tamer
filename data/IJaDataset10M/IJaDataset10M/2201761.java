package org.dbunit.database.statement;

import java.sql.SQLException;

/**
 * @author Manuel Laflamme
 * @version $Revision: 398 $
 * @since Mar 15, 2002 
 */
public interface IBatchStatement {

    void addBatch(String sql) throws SQLException;

    int executeBatch() throws SQLException;

    void clearBatch() throws SQLException;

    void close() throws SQLException;
}
