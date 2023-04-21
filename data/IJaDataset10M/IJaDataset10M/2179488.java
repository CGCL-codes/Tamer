package com.googlecode.datawander.connectors;

import com.googlecode.datawander.i18n.I18n;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import oracle.jdbc.OracleConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author Marcin Stachniuk
 */
public class OracleConnector {

    private static final Logger logger = Logger.getLogger(OracleConnector.class);

    private static String driver = "oracle.jdbc.driver.OracleDriver";

    private static String url = "jdbc:oracle:thin:@localhost:1521:xe";

    private static String username = "alltypes";

    private static String password = "123";

    /**
     * Load driver to connet to database
     * @throws MyRelationalDataBaseException
     */
    public static void initDatabaseDriver() throws MyRelationalDataBaseException {
        logger.trace("initDatabaseDriver()");
        try {
            Class.forName(driver);
            logger.debug(I18n.getString("relationaldriverloadedsucesfull"));
        } catch (ClassNotFoundException e) {
            String errorMessage = I18n.getString("relationaldriverloadedfail");
            logger.fatal(errorMessage, e);
            throw new MyRelationalDataBaseException(errorMessage);
        }
    }

    /**
     * Pobranie obiektu łaczącego z bazą Oracla
     * @return
     * @throws MyRelationalDataBaseException
     */
    public static Connection getConnection() throws MyRelationalDataBaseException {
        logger.trace("getConnection()");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
            logger.debug(I18n.getString("relationaldatabaseconnectionsucesfull"));
        } catch (SQLException ex) {
            String errorMessage = I18n.getString("relationaldatabaseconnectionfail");
            logger.fatal(errorMessage, ex);
            throw new MyRelationalDataBaseException(errorMessage);
        }
        return conn;
    }

    public static void loadSettingsFromFile(String filePath) {
        logger.trace("loadSettingsFromFile(" + filePath + ")");
    }

    public static void saveSettingsToFile(String filePath) {
        logger.trace("saveSettingsToFile(" + filePath + ")");
    }

    public static void setPassword(String password) {
        OracleConnector.password = password;
    }

    public static void setUrl(String url) {
        OracleConnector.url = url;
    }

    public static void setUsername(String username) {
        OracleConnector.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }
}
