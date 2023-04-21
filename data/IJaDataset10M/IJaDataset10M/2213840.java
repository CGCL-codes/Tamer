package org.jtools.sql.delegate;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class DelegationDriver extends AbstractDelegation<Driver> implements Driver {

    public DelegationDriver(Driver delegate) {
        super(delegate);
    }

    public boolean acceptsURL(String url) throws SQLException {
        return delegate.acceptsURL(url);
    }

    public Connection connect(String url, Properties info) throws SQLException {
        return delegate.connect(url, info);
    }

    public int getMajorVersion() {
        return delegate.getMajorVersion();
    }

    public int getMinorVersion() {
        return delegate.getMinorVersion();
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return delegate.getPropertyInfo(url, info);
    }

    public boolean jdbcCompliant() {
        return delegate.jdbcCompliant();
    }
}
