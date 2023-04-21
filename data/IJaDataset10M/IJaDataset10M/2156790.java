package dataStructure.database;

import com.mysql.jdbc.Connection;
import settings.Config;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class creates the connect, connection and disconnect method
 * for the dbQuery class.
 *
 * @author scav
 */
public class DbConnect {

    private Config conf;

    private String user;

    private String pass;

    private String host;

    private String databaseName;

    private String dbDriver = "com.mysql.jdbc.Driver";

    private Connection connection = null;

    public DbConnect() {
        conf = new Config();
        this.user = conf.getDbUsername();
        this.pass = conf.getDbPassword();
        this.host = conf.getDbHostName();
        this.databaseName = conf.getDatabaseName();
    }

    /**
     * This method creates the databaseconnection.
     * TODO: Very dirty try/catch code, must be delt with.
     */
    protected void connect() {
        try {
            Class.forName(dbDriver).newInstance();
            connection = (Connection) DriverManager.getConnection(host, user, pass);
        } catch (SQLException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (ClassNotFoundException ex) {
        }
    }

    /**
     * This method disconnects the database connection, or so it should anyways...
     */
    protected void disconnect() {
        try {
            connection.close();
            System.out.println("Connection to database closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the connection object.
     * @return connection
     */
    protected Connection connection() {
        return connection;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
