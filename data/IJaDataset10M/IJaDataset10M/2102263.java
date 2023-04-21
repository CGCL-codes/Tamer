package com.netx.generics.sql;

import javax.sql.DataSource;
import com.netx.generics.basic.Checker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.PrintWriter;

public class BasicDataSource implements DataSource {

    private final String _location;

    private final String _username;

    private final String _password;

    private int _timeout;

    private PrintWriter _logWriter;

    public BasicDataSource(String driver, String location, String username, String password) {
        Checker.checkEmpty(driver, "driver");
        Checker.checkEmpty(location, "location");
        Checker.checkEmpty(username, "username");
        Checker.checkEmpty(password, "password");
        _location = location;
        _username = username;
        _password = password;
        _timeout = 0;
        _logWriter = null;
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(driver + ": driver not found");
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(_location, username, password);
    }

    public Connection getConnection() throws SQLException {
        return getConnection(_username, _password);
    }

    public int getLoginTimeout() {
        return _timeout;
    }

    public void setLoginTimeout(int timeout) {
        this._timeout = timeout;
    }

    public PrintWriter getLogWriter() {
        return _logWriter;
    }

    public void setLogWriter(PrintWriter p) {
        _logWriter = p;
    }

    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    public <T> T unwrap(Class<T> iface) {
        return null;
    }
}
