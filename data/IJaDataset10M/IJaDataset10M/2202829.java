package com.netx.generics.R1.sql;

import java.util.Map;
import java.util.HashMap;
import com.netx.basic.R1.eh.Checker;

public class JdbcDriver {

    public static final JdbcDriver MYSQL = new JdbcDriver("MySQL", "com.mysql.jdbc.Driver", "jdbc:mysql://<server>:<port>/<schema>?autoReconnect=true", 3306);

    private static final Map<String, JdbcDriver> _drivers;

    static {
        _drivers = new HashMap<String, JdbcDriver>();
        _drivers.put(MYSQL.getName().toLowerCase(), MYSQL);
    }

    public static JdbcDriver getRegisteredDriver(String name) {
        Checker.checkEmpty(name, "name");
        return _drivers.get(name.toLowerCase());
    }

    private final String _name;

    private final String _className;

    private final String _url;

    private final int _defaultPort;

    public JdbcDriver(String name, String className, String url, int defaultPort) {
        Checker.checkEmpty(name, "name");
        Checker.checkEmpty(className, "className");
        Checker.checkEmpty(url, "url");
        Checker.checkMinValue(defaultPort, 1, "defaultPort");
        _name = name;
        _className = className;
        _url = url;
        _defaultPort = defaultPort;
    }

    public String getName() {
        return _name;
    }

    public String getClassName() {
        return _className;
    }

    public String getURL() {
        return _url;
    }

    public int getDefaultPort() {
        return _defaultPort;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof JdbcDriver) {
            JdbcDriver d = (JdbcDriver) o;
            return _name.equals(d._name) && _className.equals(d._className) && _url.equals(d._url) && _defaultPort == d._defaultPort;
        } else {
            return false;
        }
    }

    public String toString() {
        return _name + ':' + _className;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + _name.hashCode();
        hash = 31 * hash + _className.hashCode();
        hash = 31 * hash + _url.hashCode();
        hash = 31 * hash + _defaultPort;
        return hash;
    }
}
