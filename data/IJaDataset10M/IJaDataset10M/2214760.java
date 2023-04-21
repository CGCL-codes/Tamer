package com.ctp.test.configuration;

import java.util.Properties;
import com.ctp.test.util.PropertyLoader;

/**
 * 
 * @author Bartosz Majsak
 *
 */
public final class Configuration {

    private static final String CTPTEST_PROPERTIES = "ctptest.properties";

    private static final String JDBC_URL = "db.url";

    private static final String USERNAME = "db.username";

    private static final String PASSWORD = "db.password";

    private static final String INITIAL_SQL = "db.initial_sql";

    private final Properties properties;

    private DatabaseConfiguration databaseConfiguration;

    public static Configuration instance() {
        return InstanceHolder.INSTANCE;
    }

    public Configuration() {
        properties = PropertyLoader.loadProperties(CTPTEST_PROPERTIES);
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        if (null == databaseConfiguration) {
            createDatabaseConfiguration();
        }
        return databaseConfiguration;
    }

    private void createDatabaseConfiguration() {
        String dbURL = (String) properties.get(JDBC_URL);
        String username = (String) properties.get(USERNAME);
        String password = (String) properties.get(PASSWORD);
        String initialSql = (String) properties.get(INITIAL_SQL);
        databaseConfiguration = new DatabaseConfiguration(dbURL, username, password);
        databaseConfiguration.setInitStatement(initialSql);
    }

    private static class InstanceHolder {

        private static final Configuration INSTANCE = new Configuration();
    }
}
