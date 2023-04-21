package cranix.board.util;

import java.sql.Connection;
import cranix.common.util.SuperConnectionManager;

public class ConnectionManager extends SuperConnectionManager {

    private static ConnectionManager cm = null;

    public static final String name = "jdbc/mysql";

    protected ConnectionManager() throws Exception {
        super();
    }

    public static ConnectionManager getInstance() throws Exception {
        if (cm == null) cm = new ConnectionManager();
        return cm;
    }

    public Connection getConnection() throws Exception {
        return openConnection(name);
    }

    public Connection openConnection(String name) throws Exception {
        return super.openConnection(name);
    }

    public void closeConnection(Connection conn) throws Exception {
        super.closeConnection(conn);
    }
}
