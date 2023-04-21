package biblioteca.dao.connection;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

public class ThreadLocalDataSource implements DataSource {

    private ThreadLocal<Connection> connections = new ThreadLocal<Connection>();

    private static ThreadLocalDataSource instance;

    public static synchronized ThreadLocalDataSource getInstance() {
        if (instance == null) {
            instance = new ThreadLocalDataSource();
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection c;
        c = connections.get();
        if (c == null) {
            c = DriverManager.getConnection("jdbc:mysql://localhost/biblioteca", "root", "raquel");
            c.setAutoCommit(false);
            connections.set(c);
        }
        return c;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection c;
        try {
            c = connections.get();
            if (c == null) {
                c = DriverManager.getConnection("jdbc:mysql://localhost/biblioteca", username, password);
                connections.set(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public void remove() {
        connections.remove();
    }

    @Override
    public PrintWriter getLogWriter() throws UnsupportedOperationException {
        return null;
    }

    @Override
    public int getLoginTimeout() throws UnsupportedOperationException {
        return 0;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws UnsupportedOperationException {
    }

    @Override
    public void setLoginTimeout(int seconds) throws UnsupportedOperationException {
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws UnsupportedOperationException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws UnsupportedOperationException {
        return null;
    }
}
