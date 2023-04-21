package info.jmonit.support.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

/**
 * @author <a href="mailto:ndeloof@sourceforge.net">ndeloof</a>
 *
 */
public class Jdbc2MonitoredPreparedStatement extends Jdbc2MonitoredStatement implements PreparedStatement {

    /**
     * @param statement target statement
     * @param query SQL Query
     * @param connection monitored connexion
     */
    public Jdbc2MonitoredPreparedStatement(CallableStatement statement, String query, MonitoredConnection connection) {
        super(statement, query, connection);
    }

    /**
     * @param statement target statement
     * @param query SQL Query
     * @param connection monitored connexion
     */
    public Jdbc2MonitoredPreparedStatement(PreparedStatement statement, String query, MonitoredConnection connection) {
        super(statement, query, connection);
    }
}
