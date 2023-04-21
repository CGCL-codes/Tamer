package com.googlecode.jerato.library.store;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import com.googlecode.jerato.library.ExternalClassLoader;

public class ExternalJdbcDriver implements Driver {

    protected HashMap _driverMap = new HashMap();

    static {
        try {
            DriverManager.registerDriver(new ExternalJdbcDriver());
        } catch (SQLException se) {
        }
    }

    public ExternalJdbcDriver() {
    }

    protected Driver loadDriver(String url) {
        if (url == null) {
            return null;
        }
        if (!url.startsWith("jdbc:external:")) {
            return null;
        }
        String options = url.substring(14);
        String[] optionArray = options.split(";");
        if (optionArray.length < 3) {
            return null;
        }
        String className = optionArray[0];
        String classPath = optionArray[1];
        ExternalClassLoader loader = new ExternalClassLoader(new String[] { classPath });
        Class clazz = null;
        try {
            clazz = loader.loadClass(className, true);
        } catch (ClassNotFoundException ce) {
            return null;
        }
        Driver driver = null;
        try {
            driver = (Driver) clazz.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
        _driverMap.put(url, driver);
        return driver;
    }

    public boolean acceptsURL(String url) throws SQLException {
        if (url == null) {
            return false;
        }
        if (!url.startsWith("jdbc:external:")) {
            return false;
        }
        if (_driverMap.containsKey(url)) {
            return true;
        }
        Driver driver = loadDriver(url);
        return (driver != null);
    }

    public Connection connect(String url, Properties info) throws SQLException {
        Driver driver = null;
        if (_driverMap.containsKey(url)) {
            driver = (Driver) _driverMap.get(url);
        } else {
            driver = loadDriver(url);
            if (driver == null) {
                throw new SQLException();
            }
        }
        String options = url.substring(14);
        String[] optionArray = options.split(";");
        if (optionArray.length < 3) {
            throw new SQLException();
        }
        String originalUrl = optionArray[2];
        return driver.connect(originalUrl, info);
    }

    public int getMajorVersion() {
        return 1;
    }

    public int getMinorVersion() {
        return 0;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return null;
    }

    public boolean jdbcCompliant() {
        return false;
    }
}
