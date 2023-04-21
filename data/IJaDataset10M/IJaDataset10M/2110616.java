package org.dbunit.database.statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Laflamme
 * @version $Revision: 920 $
 * @since Feb 20, 2002
 */
public class SimpleStatement extends AbstractBatchStatement {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(SimpleStatement.class);

    private final List _list = new ArrayList();

    SimpleStatement(Connection connection) throws SQLException {
        super(connection);
    }

    public void addBatch(String sql) throws SQLException {
        logger.debug("addBatch(sql={}) - start", sql);
        _list.add(sql);
    }

    public int executeBatch() throws SQLException {
        logger.debug("executeBatch() - start");
        int result = 0;
        for (int i = 0; i < _list.size(); i++) {
            String sql = (String) _list.get(i);
            if (logger.isDebugEnabled()) logger.debug("DbUnit SQL: " + sql);
            boolean r = _statement.execute(sql);
            if (!r) {
                result += _statement.getUpdateCount();
            }
        }
        return result;
    }

    public void clearBatch() throws SQLException {
        logger.debug("clearBatch() - start");
        _list.clear();
    }
}
