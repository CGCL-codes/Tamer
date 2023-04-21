package net.sf.dropboxmq.workflow.persistence.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created: 04 Feb 2011
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision$, $Date$
 */
public class JDBCHelper {

    private static final Log log = LogFactory.getLog(JDBCHelper.class);

    private static final String SELECT_LAST_INSERT_ID = "select LAST_INSERT_ID()";

    public int getLastInsertId(final Connection connection) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            return selectSingleInt(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(statement);
        }
    }

    public int selectSingleInt(final PreparedStatement statement) throws SQLException {
        final ResultSet results = statement.executeQuery();
        if (!results.next()) {
            throw new RuntimeException("Selecting a single int did not return a single row");
        }
        final int result = results.getInt(1);
        if (results.next()) {
            throw new RuntimeException("Selecting a single int returned multiple rows");
        }
        return result;
    }

    public void safeClose(final PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.warn("SQLException while calling PreparedStatement.close(), " + e.getMessage());
            }
        }
    }
}
