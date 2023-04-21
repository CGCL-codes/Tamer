package org.lokee.punchcard.persistence.jdbc.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.digester.Digester;
import org.lokee.punchcard.IPunchCardMessages;
import org.lokee.punchcard.PunchCardRunTimeException;
import org.lokee.punchcard.persistence.IPersistentMessages;
import org.lokee.punchcard.util.ILogSeverity;
import org.lokee.punchcard.util.ResourceManager;
import org.lokee.punchcard.util.Utilities;
import org.xml.sax.SAXException;

/**
 * @author CLaguerre
 * 
 */
public abstract class ConnectionUtil {

    private static ConnectionConfigs connectionConfigs;

    private static final String globalConnectionPropertiesName = ConnectionConfig.class.getName() + "#GLOBAL-CONNECTION-PROPERTIES";

    public static String[] fetchAllDriverClassNames() {
        if (connectionConfigs == null) {
            try {
                buildConnectionConfigs();
            } catch (IOException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": trying to retrieve driver classes.", ILogSeverity.FATAL, e);
            } catch (SAXException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": trying to retrieve driver classes.", ILogSeverity.FATAL, e);
            }
        }
        return connectionConfigs.getAllDriverClasses();
    }

    public static ConnectionConfig fetchGlobalConnectionConfig() throws PunchCardRunTimeException {
        if (connectionConfigs == null) {
            try {
                buildConnectionConfigs();
            } catch (IOException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": name(globalConnectionProperties)", ILogSeverity.FATAL, e);
            } catch (SAXException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": name(globalConnectionProperties)", ILogSeverity.FATAL, e);
            }
        }
        return connectionConfigs.get(globalConnectionPropertiesName);
    }

    public static void addConnectionConfig(ConnectionConfig connectionConfig) throws PunchCardRunTimeException {
        if (connectionConfigs == null) {
            try {
                buildConnectionConfigs();
            } catch (IOException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": name(globalConnectionProperties)", ILogSeverity.FATAL, e);
            } catch (SAXException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": name(globalConnectionProperties)", ILogSeverity.FATAL, e);
            }
        }
        connectionConfigs.addConnectionConfig(connectionConfig);
    }

    public static ConnectionConfig fetchConnectionConfig(String name) throws PunchCardRunTimeException {
        if (connectionConfigs == null) {
            try {
                buildConnectionConfigs();
            } catch (IOException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": name(" + name + ")", ILogSeverity.FATAL, e);
            } catch (SAXException e) {
                throw new PunchCardRunTimeException(IPersistentMessages.E_ERROR_CONSTRUCTING_PERSISTENCE_CONFIG + ": name(" + name + ")", ILogSeverity.FATAL, e);
            }
        }
        if (connectionConfigs.get(name) == null) {
            return getDBConnectionConfig(name);
        }
        return connectionConfigs.get(name);
    }

    private static ConnectionConfig getDBConnectionConfig(String name) throws ConnectionException {
        String dbKey = ResourceManager.getResource("PUNCHCARD.CONNECTION_MANAGER.DB.DBKEY");
        if (connectionConfigs.get(dbKey) == null) {
            return null;
        }
        String selectConnectionSQL = ResourceManager.getResource("PUNCHCARD.CONNECTION_MANAGER.DB.SELECT.SQL.CONNECTIONS");
        String urlColumn = ResourceManager.getResource("PUNCHCARD.CONNECTION_MANAGER.DB.URL_COLUMN_NAME");
        String userIdColumn = ResourceManager.getResource("PUNCHCARD.CONNECTION_MANAGER.DB.USERID_COLUMN_NAME");
        String passwordColumn = ResourceManager.getResource("PUNCHCARD.CONNECTION_MANAGER.DB.PASSWORD_COLUMN_NAME");
        String datasourceColumn = ResourceManager.getResource("PUNCHCARD.CONNECTION_MANAGER.DB.DATASOURCE_COLUMN_NAME");
        selectConnectionSQL = selectConnectionSQL.replace("${DBKEY}", name);
        Connection connection = ConnectionFactory.getInstance().fetchConnection(connectionConfigs.get(dbKey));
        PreparedStatement stmt = null;
        ResultSet set = null;
        try {
            stmt = connection.prepareStatement(selectConnectionSQL);
            set = stmt.executeQuery();
            if (set.next()) {
                String url = set.getString(urlColumn);
                String userId = set.getString(userIdColumn);
                String password = set.getString(passwordColumn);
                String datasource = set.getString(datasourceColumn);
                return new ConnectionConfig(name, userId, password, url, datasource);
            }
        } catch (SQLException e) {
            throw new ConnectionException(IPunchCardMessages.E_ERROR_INSTALL_DRIVER + " : trying to get drivers", ILogSeverity.FATAL, e);
        } finally {
            ConnectionUtil.closeResultSet(set);
            ConnectionUtil.closeStatement(stmt);
            ConnectionUtil.closeConnection(connection);
        }
        return null;
    }

    private static void buildConnectionConfigs() throws IOException, SAXException {
        Utilities.getLogger(ConnectionUtil.class).debug(Utilities.fetchClassLoaderResource(ResourceManager.getResource("connection.config")));
        InputStream is = Utilities.fetchClassLoaderResource(ResourceManager.getResource("connection.config")).openStream();
        Digester digester = new Digester();
        connectionConfigs = new ConnectionConfigs();
        digester.push(connectionConfigs);
        digester.setValidating(false);
        digester.addSetProperties("connection-manager");
        digester.addObjectCreate("connection-manager/global-connection-properties", ConnectionConfig.class.getName(), "className");
        digester.addSetProperties("connection-manager/global-connection-properties");
        digester.addSetNext("connection-manager/global-connection-properties", "addGlobalConnectionConfig", ConnectionConfig.class.getName());
        digester.addObjectCreate("connection-manager/connection-properties", ConnectionConfig.class.getName(), "className");
        digester.addSetProperties("connection-manager/connection-properties");
        digester.addSetNext("connection-manager/connection-properties", "addConnectionConfig", ConnectionConfig.class.getName());
        digester.addCallMethod("connection-manager/drivers/driver-class", "addDriverClass", 1);
        digester.addCallParam("connection-manager/drivers/driver-class", 0);
        digester.addCallMethod("*/property", "setProperty", 2);
        digester.addCallParam("*/property", 0, "name");
        digester.addCallParam("*/property", 1);
        digester.parse(is);
    }

    private static class ConnectionConfigs extends LinkedHashMap<String, ConnectionConfig> {

        /**
	 * 
	 */
        private static final long serialVersionUID = 1055443832168873141L;

        private final List<String> drivers;

        public ConnectionConfigs() {
            drivers = new ArrayList<String>();
        }

        public String[] getAllDriverClasses() {
            return drivers.toArray(new String[drivers.size()]);
        }

        public void addDriverClass(String driver) {
            drivers.add(driver);
        }

        public void addGlobalConnectionConfig(ConnectionConfig connectionConfig) {
            connectionConfig.setName(globalConnectionPropertiesName);
            this.put(connectionConfig.getName(), connectionConfig);
        }

        public void addConnectionConfig(ConnectionConfig connectionConfig) {
            this.put(connectionConfig.getName(), connectionConfig);
        }
    }

    /**
     * This method trys to close the database connection and logs a message if
     * it is unsuccessful in doing so.
     * 
     * @param conn -
     *                the connection to close
     */
    public static void closeConnection(Connection conn) {
        try {
            if ((conn != null) && !conn.getAutoCommit()) {
                conn.rollback();
            }
        } catch (Throwable sqlx) {
            Utilities.getLogger(ConnectionUtil.class).error("E_DATABASE_ERROR_SET_AUTOCOMMIT", sqlx);
        }
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Throwable exp) {
            Utilities.getLogger(ConnectionUtil.class).info("E_DATABASE_ERROR_CONNECTION_NOT_CLOSED", exp);
        }
    }

    /**
     * This method trys to close the Statement and logs a message if it is
     * unsuccessful in doing so.
     * 
     * @param rs -
     *                the <code>ResultSet</code> to close
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Throwable exp) {
            Utilities.getLogger(ConnectionUtil.class).info("(ILogMessages.E_DATABASE_ERROR_CLOSING_RESULT_SET", exp);
        }
    }

    /**
     * This method trys to close the Statement and logs a message if it is
     * unsuccessful in doing so.
     * 
     * @param stmt -
     *                the statement to close
     */
    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (Throwable exp) {
            Utilities.getLogger(ConnectionUtil.class).info("E_DATABASE_ERROR_PREPARED_STATEMENT", exp);
        }
    }
}
