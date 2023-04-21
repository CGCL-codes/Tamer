package org.dbunit.database.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.TypeCastException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Manuel Laflamme
 * @version $Revision: 672 $
 * @since Mar 16, 2002
 */
public class SimplePreparedStatement extends AbstractPreparedBatchStatement {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(SimplePreparedStatement.class);

    private int _index;

    private int _result;

    public SimplePreparedStatement(String sql, Connection connection) throws SQLException {
        super(sql, connection);
        _index = 0;
        _result = 0;
    }

    public void addValue(Object value, DataType dataType) throws TypeCastException, SQLException {
        logger.debug("addValue(value={}, dataType={}) - start", value, dataType);
        if (value == null || value == ITable.NO_VALUE) {
            _statement.setNull(++_index, dataType.getSqlType());
            return;
        }
        dataType.setSqlValue(value, ++_index, _statement);
    }

    public void addBatch() throws SQLException {
        logger.debug("addBatch() - start");
        boolean result = _statement.execute();
        if (!result) {
            _result += _statement.getUpdateCount();
        }
        _index = 0;
    }

    public int executeBatch() throws SQLException {
        logger.debug("executeBatch() - start");
        int result = _result;
        clearBatch();
        return result;
    }

    public void clearBatch() throws SQLException {
        logger.debug("clearBatch() - start");
        _index = 0;
        _result = 0;
    }
}
