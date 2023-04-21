package yajcp.core.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    public static Connection connection;

    private static String host = null;

    private static String username = null;

    private static String password = null;

    private static String database = null;

    private static Integer port = 3306;

    private static Boolean connectionStatus = false;

    private static String connectionStatusMessage = "";

    public MySQL() {
        Configuration c = new Configuration(true);
        new MySQL(c.find("mysql_host"), c.find("mysql_username"), c.find("mysql_password"), c.find("mysql_database"), c.find("mysql_port"));
    }

    public MySQL(String host, String username, String password, String database, Integer port) {
        MySQL.host = host;
        MySQL.username = username;
        MySQL.password = password;
        MySQL.database = database;
        MySQL.port = port;
        String DbUrl = "jdbc:mysql://" + MySQL.host + ":" + MySQL.port + "/" + MySQL.database;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            MySQL.connection = DriverManager.getConnection(DbUrl, MySQL.username, MySQL.password);
            if (MySQL.connection != null) {
                setConnectionStatus(false);
                setConnectionStatusMessage("Verbunden");
            }
        } catch (SQLException sqlex) {
            setConnectionStatus(false);
            setConnectionStatusMessage(sqlex.getMessage());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } catch (Exception ex) {
            setConnectionStatus(false);
            setConnectionStatusMessage("Unable to find driver \"" + ex.getMessage() + "\"");
        }
    }

    /**
     * @param host
     * @param username
     * @param password
     * @param database
     * @param port
     * @throws NumberFormatException
     */
    public MySQL(String host, String username, String password, String database, String port) throws NumberFormatException {
        new MySQL(host, username, password, database, Integer.parseInt(port));
    }

    public static void disconnect() {
        try {
            MySQL.connection.close();
        } catch (SQLException sqlex) {
            setConnectionStatus(false);
            setConnectionStatusMessage(sqlex.getMessage());
        }
    }

    public static void setConnectionStatus(Boolean connectionStatus) {
        MySQL.connectionStatus = connectionStatus;
    }

    public static Boolean getConnectionStatus() {
        return MySQL.connectionStatus;
    }

    public static void setConnectionStatusMessage(String connectionStatusMessage) {
        MySQL.connectionStatusMessage = connectionStatusMessage;
    }

    public static String getConnectionStatusMessage() {
        return MySQL.connectionStatusMessage;
    }
}
