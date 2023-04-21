package org.equanda.reporting.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Implementation of java.sql.Driver used by jasper reports
 *
 * @author NetRom team
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class EquandaDriver implements Driver {

    static {
        try {
            java.sql.DriverManager.registerDriver(new EquandaDriver());
        } catch (SQLException ex) {
        }
    }

    private static final String PROTOCOL_PREFIX = "jdbc:equanda:";

    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 1;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    public boolean acceptsURL(String s) throws SQLException {
        return s.startsWith(PROTOCOL_PREFIX);
    }

    public Connection connect(String s, Properties properties) throws SQLException {
        if (!s.startsWith(PROTOCOL_PREFIX)) return null;
        return new EquandaConnection(s.substring(PROTOCOL_PREFIX.length()));
    }

    public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
        return null;
    }
}
