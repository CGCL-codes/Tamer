package org.jawa.database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.datasource.SmartDataSource;

public class JawaDataSource implements SmartDataSource {

    private ConnectionProvider provider;

    public JawaDataSource(ConnectionProvider provider) {
        this.provider = provider;
    }

    public Connection getConnection() throws SQLException {
        return provider.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter printwriter) throws SQLException {
    }

    public void setLoginTimeout(int j) throws SQLException {
    }

    public int getLoginTimeout() throws SQLException {
        return 1000;
    }

    public Object unwrap(Class iface) {
        return isWrapperFor(iface) ? this : null;
    }

    public boolean shouldClose(Connection con) {
        return true;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
