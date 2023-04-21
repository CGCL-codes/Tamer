package es.devel.opentrats.booking.service.business.impl;

import es.devel.opentrats.booking.service.business.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author Fran Serrano
 */
public class ConnectionServiceImpl implements IConnectionService {

    private static final int POOL_ACQUIRE_INCREMENT = 1;

    private static final String CONNECTION_URL_PREFIX = "jdbc:mysql://";

    private static final int POOL_INITIAL_SIZE = 1;

    private static final int POOL_MAX_SIZE = 10;

    private static final String USER_NAME = "usuario";

    private static final String CONNECTION_URL = "servidor:3306/opentrats";

    private static final String USER_PASSWORD = "Password";

    private static ConnectionServiceImpl ref = null;

    private static ComboPooledDataSource cpds;

    /** Creates a new instance of ConnectionManager */
    @SuppressWarnings("static-access")
    protected ConnectionServiceImpl() {
        try {
            this.cpds = new ComboPooledDataSource();
            this.cpds.setDriverClass("com.mysql.jdbc.Driver");
            this.cpds.setJdbcUrl(CONNECTION_URL_PREFIX + CONNECTION_URL);
            this.cpds.setUser(USER_NAME);
            this.cpds.setPassword(USER_PASSWORD);
            this.cpds.setAcquireIncrement(POOL_ACQUIRE_INCREMENT);
            this.cpds.setInitialPoolSize(POOL_INITIAL_SIZE);
            this.cpds.setMaxPoolSize(POOL_MAX_SIZE);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized ConnectionServiceImpl getInstance() {
        if (ref != null) {
            return ref;
        } else {
            return new ConnectionServiceImpl();
        }
    }

    public Connection getConnection() {
        try {
            return this.cpds.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getRootLogger().error(ex);
            return null;
        }
    }

    public void CloseConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void CloseConnetion(Connection connection) {
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static void CloseAll() {
        cpds.close();
    }
}
