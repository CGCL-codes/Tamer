package org.oclc.da.properties;

/**
 * This class determines the default state of the database.
 * Based on this property the application knows if updates are required.
 * @author stanesca
 * Created on Jan 13, 2005
 */
public class DefaultDBProps {

    /** This is database configuration properties file. 
     */
    private static final String DB_PROPS = "/config/default.db.properties";

    /** Database connection JDBC driver  */
    public static final String DATABASE_TYPE = PropertiesManager.getProperty(DB_PROPS, "database.type");

    /** Database connection JDBC driver  */
    public static final String JDBC_DRIVER = PropertiesManager.getProperty(DB_PROPS, "jdbc.driver");

    /** Database connection url  */
    public static final String JDBC_URL = PropertiesManager.getProperty(DB_PROPS, "jdbc.url");

    /** Database connection user  */
    public static final String JDBC_USER = PropertiesManager.getProperty(DB_PROPS, "jdbc.user");

    /** Database connection credentials  */
    public static final String JDBC_PASSWORD = PropertiesManager.getProperty(DB_PROPS, "jdbc.password");

    /** Current database version.  */
    public static final int DB_VERSION = (int) PropertiesManager.getNumericProperty(DB_PROPS, "db.version").longValue();
}
