package quickfix;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLLog extends JdbcLog {

    public MySQLLog(SessionSettings settings, SessionID sessionID) throws SQLException, ClassNotFoundException, ConfigError {
        super(settings, sessionID);
    }

    public Connection connect(SessionSettings settings, SessionID sessionID) throws SQLException, ClassNotFoundException, ConfigError {
        return JdbcUtil.openMySQLConnection(settings, sessionID);
    }
}
