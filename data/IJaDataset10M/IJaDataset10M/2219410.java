package org.dbunit.database.statement;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.TypeCastException;
import java.sql.SQLException;

/**
 * @author Manuel Laflamme
 * @version $Revision: 398 $
 * @since Mar 15, 2002
 */
public interface IPreparedBatchStatement {

    void addValue(Object value, DataType dataType) throws TypeCastException, SQLException;

    void addBatch() throws SQLException;

    int executeBatch() throws SQLException;

    void clearBatch() throws SQLException;

    void close() throws SQLException;
}
