package org.opu.db_vdumper.beans;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author yura
 */
public class UserData {

    public static final Set<Class<? extends JDBCProperties>> AVAILABLE_JDB;

    static {
        AVAILABLE_JDB = new HashSet<Class<? extends JDBCProperties>>();
        AVAILABLE_JDB.add(PostgresJDBCProperties.class);
    }

    public static final String DB_HOST_PROP_LABEL = "db.host";

    public static final String DB_NAME_PROP_LABEL = "db.name";

    public static final String DB_PORT_PROP_LABEL = "db.port";

    public static final String USER_PASSWORD_PROP_LABEL = "user.password";

    public static final String USER_USER_PROP_LABEL = "user.user";

    private String user;

    private String password;

    private String dbName;

    private String dbHost;

    private String dbPort;

    private JDBCProperties dbProperties;

    public UserData(Properties prop) {
        user = prop.getProperty(USER_USER_PROP_LABEL, "");
        password = prop.getProperty(USER_PASSWORD_PROP_LABEL, "");
        dbName = prop.getProperty(DB_NAME_PROP_LABEL, "");
        dbHost = prop.getProperty(DB_HOST_PROP_LABEL, "");
        dbPort = prop.getProperty(DB_PORT_PROP_LABEL, "");
        try {
            String name = PostgresJDBCProperties.class.getName();
            String property = prop.getProperty(DB_PORT_PROP_LABEL, name);
            dbProperties = (JDBCProperties) Class.forName(property).newInstance();
        } catch (Exception ex) {
            try {
                dbProperties = UserData.AVAILABLE_JDB.iterator().next().newInstance();
            } catch (Exception ex2) {
            }
        }
    }

    public UserData(String user, String password, String dbName, String dbHost, String dbPort, JDBCProperties dbProperties) {
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbProperties = dbProperties;
    }

    public UserData(String user, String password, String dbName, String dbHost, JDBCProperties dbProperties) {
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.dbHost = dbHost;
        this.dbPort = dbProperties.getDefaultPort();
        this.dbProperties = dbProperties;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbPort() {
        return dbPort;
    }

    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public JDBCProperties getDbProperties() {
        return dbProperties;
    }

    public void setDbProperties(JDBCProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Collect all data in properties
     * @param savePswd if true added password to properties
     * @return Properties
     */
    public Properties properties(boolean savePswd) {
        Properties p = new Properties();
        p.setProperty(USER_USER_PROP_LABEL, user);
        if (savePswd) {
            p.setProperty(USER_PASSWORD_PROP_LABEL, password);
        }
        p.setProperty(DB_NAME_PROP_LABEL, dbName);
        p.setProperty(DB_HOST_PROP_LABEL, dbHost);
        p.setProperty(DB_PORT_PROP_LABEL, dbPort);
        if (dbProperties != null) {
            p.putAll(dbProperties.properties());
        }
        return p;
    }

    /**
     *  Try to find propertie {@link JDBCProperties#PROPERTIES_CLASS_NAME}
     * and create new instance by class name:<pre>
     *  (JDBCProperties) Class.forName(p
     *          .getProperty(JDBCProperties.PROPERTIES_CLASS_NAME))
     *              .newInstance();
     * </pre>
     * @param p properties
     * @return
     */
    public static JDBCProperties getProperties(Properties p) {
        if (p == null) {
            return null;
        }
        String cname = p.getProperty(JDBCProperties.PROPERTIES_CLASS_NAME);
        if (cname != null && !cname.isEmpty()) {
            try {
                return (JDBCProperties) Class.forName(cname).newInstance();
            } catch (ClassNotFoundException ex) {
            } catch (InstantiationException ex) {
            } catch (IllegalAccessException ex) {
            }
        }
        return null;
    }

    /**
     *  Database connect properies
     */
    public static interface JDBCProperties {

        public static final String PROPERTIES_CLASS_NAME = "jdbc.class.propclass";

        public static final String CLASS_NAME = "jdbc.prop.classname";

        public static final String DEFAULT_PORT = "jdbc.prop.defaultport";

        public static final String NAME = "jdbc.prop.name";

        /**
         *  To load driver static sections
         * @return driver name
         * @see Class#forName(java.lang.String) 
         */
        public String getClassName();

        /**
         *  Return curent database default port, for example
         * for postgresql it`s 5432
         * @return curent database default port
         */
        public String getDefaultPort();

        /**
         *  Return database name, for example: postgresql
         * ot mysql ...
         * @return database name
         */
        public String getName();

        /**
         *  Create properties from file
         * @return properties from file
         */
        public Properties properties();
    }

    /**
     * PostgreSQL database connect properties
     */
    public static class PostgresJDBCProperties implements JDBCProperties {

        @Override
        public String getClassName() {
            return "org.postgresql.Driver";
        }

        @Override
        public String getDefaultPort() {
            return "5432";
        }

        @Override
        public String getName() {
            return "postgresql";
        }

        @Override
        public Properties properties() {
            Properties prop = new Properties();
            prop.setProperty(PROPERTIES_CLASS_NAME, getClass().getName());
            prop.setProperty(CLASS_NAME, getClassName());
            prop.setProperty(DEFAULT_PORT, getDefaultPort());
            prop.setProperty(NAME, getName());
            return prop;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
