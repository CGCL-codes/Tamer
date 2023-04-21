package com.whstudio.common.memory.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author wbwanggp
 *
 */
public class ConnectionPool extends GenericPool<Connection> {

    public static final String JDBC_DRIVER = WhstudioConf.getString("ConnectionPool.JDBC_DRIVER");

    public static final String CONNECTION_URL = WhstudioConf.getString("ConnectionPool.CONNECTION_URL");

    public static final String USER_NAME = WhstudioConf.getString("ConnectionPool.USER_NAME");

    public static final String USER_PWD = WhstudioConf.getString("ConnectionPool.USER_PWD");

    private static ConnectionPool pool;

    private static String path = "conf/pool/ConnectionPool.xml";

    /**
	 * @param uri
	 */
    private ConnectionPool() {
        super(path, Connection.class.getName());
    }

    public static final ConnectionPool getPool() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    @Override
    protected Connection newInstance() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(CONNECTION_URL, USER_NAME, USER_PWD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    protected void beforeInit() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void afterCallback(Connection conn) {
        afterOverFlow(conn);
    }

    protected void afterOverFlow(Connection conn) {
        close(conn);
        super.afterOverFlow(conn);
    }

    public void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
        }
    }

    public void viewStatus(Connection o) {
        super.viewStatus(o);
        try {
            System.out.print("closed:" + o.isClosed() + "\n");
        } catch (SQLException e) {
        }
    }

    public void finalize() {
        close(this.inPool);
        close(this.outPool);
    }

    private void close(List<Connection> pool) {
        for (Iterator<Connection> it = pool.iterator(); it.hasNext(); ) {
            Connection conn = it.next();
            close(conn);
        }
    }
}
