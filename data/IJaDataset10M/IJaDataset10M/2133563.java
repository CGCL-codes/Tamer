package net.sf.btb;

import java.sql.Connection;
import java.sql.SQLException;
import net.sf.btb.ConnectionManager;

/**
 * 
 * <p>
 * Manage a single connection for all bridge instance and any other object
 * needing a connection to a database. The connector creates a single connection
 * with autoCommit set to false. Upon further calls to openConnection, instead
 * of creating a new connection, it increments a stack counter and returns the
 * connection already created. Upon closeConnection, the stack counter is
 * decrased and when it reaches 0, commit the transaction, close the connection
 * and set it to null. If a rollback is called, the Connector rollback the
 * transaction, close and set the connection to null and reset the stack counter
 * to 0.
 * </p>
 * <p>
 * <ul>
 * <li><b>database.url=<i>connection string</i></b> Defines the default
 * connection string. The attribute is required for the connection manager to
 * work properly.</li>
 * <li><b>database.url.{name}=<i>connection string</i></b> It's possible to
 * define as many connection strings as you want provided that you specify a
 * different <i>name</i> for each.</li>
 * <li><b>database.default=<i>{name}</i></b> If you want another connection
 * string to be used by default, specify it's name with this attribute. Note
 * that the <i>database.url.</i> part is omitted.</li>
 * </ul>
 * </p>
 * <p>
 * You can change the path of the database.properties file by setting the system
 * property attribute "databases.properties.path" to the desired file path.
 * </p>
 * 
 * @author Jean-Philippe Gravel
 * 
 */
@SuppressWarnings("deprecation")
public class Connector {

    private static Connector instance = null;

    public static Connector getInstance() {
        if (instance == null) instance = new Connector();
        return instance;
    }

    public static Connection openConnection() throws SQLException {
        return Connector.getInstance().open();
    }

    public static void rollBack() {
        Connector.getInstance().rollback();
    }

    public static void closeConnection() {
        Connector.getInstance().close();
    }

    private Connection connection;

    private int connectionStack;

    private Connector() {
        this.connection = null;
        this.connectionStack = 0;
    }

    public Connection open() throws SQLException {
        if (this.connectionStack == 0) {
            ConnectionManager cm = new ConnectionManager();
            this.connection = cm.createConnection();
            this.connection.setAutoCommit(false);
        }
        this.connectionStack++;
        return this.connection;
    }

    public void close() {
        if (this.connectionStack == 0) return;
        try {
            this.connectionStack--;
            if (this.connectionStack == 0) {
                this.connection.commit();
                this.connection.close();
                this.connection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            if (this.connectionStack > 0) {
                this.connectionStack = 0;
                this.connection.rollback();
                this.connection.close();
                this.connection = null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
