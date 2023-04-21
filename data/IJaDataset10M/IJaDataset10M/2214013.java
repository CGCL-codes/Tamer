package org.fantasy.common.db.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * 数据源
 * @author 王文成
 * @version 1.0
 * @since 2011-3-30
 */
public class OlapDataSource implements DataSource {

    private ConnectionManager manager;

    public OlapDataSource(ConnectionManager manager) {
        this.manager = manager;
    }

    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("getLoginTimeout");
    }

    public void setLoginTimeout(int timeout) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("getLogWriter");
    }

    public void setLogWriter(PrintWriter pw) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    public Connection getConnection() throws SQLException {
        DBOptions options = manager.getOptions();
        Connection conn = manager.getConnection(options.getTimeout());
        return new OlapConnectionProxy(manager, conn);
    }

    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("getConnection(String username, String password)");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("isWrapperFor");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("unwrap");
    }
}
