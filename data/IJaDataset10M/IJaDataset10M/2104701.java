package cn.ibm.onehao.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.sql.DataSource;

/**
 * @author onehao
 *
 */
public class MyDataSource2 implements DataSource {

    private static String url = "jdbc:mysql://localhost:3306/jdbc";

    private static String user = "root";

    private static String password = "123456";

    private static int initCount = 1;

    private static int maxCount = 1;

    int currentCount = 0;

    LinkedList<Connection> connectionsPool = new LinkedList<Connection>();

    public MyDataSource2() {
        try {
            for (int i = 0; i < initCount; i++) this.connectionsPool.addLast(this.createConnection());
            this.currentCount++;
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Connection getConnection() throws SQLException {
        synchronized (connectionsPool) {
            if (this.connectionsPool.size() > 0) return this.connectionsPool.removeFirst();
            if (this.currentCount < maxCount) {
                this.currentCount++;
                return this.createConnection();
            }
            throw new SQLException("��û������");
        }
    }

    public void free(Connection conn) {
        this.connectionsPool.addLast(conn);
    }

    private Connection createConnection() throws SQLException {
        Connection realConn = DriverManager.getConnection(url, user, password);
        MyConnectionHandler proxy = new MyConnectionHandler(this);
        return proxy.bind(realConn);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }
}
