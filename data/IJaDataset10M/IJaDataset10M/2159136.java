package com.rapidweb.generator.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.rapidweb.generator.core.ConfigurationManager;

/**
 * @author yxli
 * 
 */
public class DatabaseProvider {

    private static DatabaseProvider databaseProvider = null;

    private Connection conn = null;

    private ConfigurationManager configurationManager = null;

    private DatabaseProvider() {
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public static synchronized DatabaseProvider getInstance() {
        if (databaseProvider == null) {
            databaseProvider = new DatabaseProvider();
        }
        return databaseProvider;
    }

    public Connection getConnection() {
        try {
            if (configurationManager != null && (conn == null || conn.isClosed())) {
                String driverName = configurationManager.getPropertyValue("DB_DRIVER_NAME");
                Class.forName(driverName).newInstance();
                String url = configurationManager.getPropertyValue("DB_URL");
                String username = configurationManager.getPropertyValue("DB_USERNAME");
                String password = configurationManager.getPropertyValue("DB_PASSWORD");
                conn = DriverManager.getConnection(url, username, password);
            }
            return conn;
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            System.err.println(ex.getMessage());
        } catch (InstantiationException ex) {
            System.err.println(ex.getMessage());
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }
}
