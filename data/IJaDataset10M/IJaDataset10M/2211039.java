package cz.zcu.fav.hofhans.packer.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import cz.zcu.fav.hofhans.packer.exception.ValidationException;
import cz.zcu.fav.hofhans.packer.exception.ValidationException.ValidationExceptionCode;

/**
 * Singleton for getting db connection.
 * 
 * @author Tomáš Hofhans
 * @since 2.1.2009
 */
public class ConnectionManager {

    private static final Logger LOG = Logger.getLogger(ConnectionManager.class.getName());

    private static ConnectionManager instance;

    private Properties setting;

    private Connection con;

    /**
   * Hidden constructor.
   * @param dbUri database uri
   */
    private ConnectionManager() {
        super();
        setting = new Properties();
    }

    /**
   * Initialize connection.
   * @throws ValidationException validation problem
   */
    public void init() throws ValidationException {
        try {
            setting.load(new FileInputStream("config/connectionManager.properties"));
            String driver = setting.getProperty("driver");
            String dbUri = setting.getProperty("dbUri");
            String user = setting.getProperty("user");
            String pass = setting.getProperty("pass");
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(dbUri, user, pass);
            con.setAutoCommit(false);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Problem with loading jdbc driver.", e);
            throw new ValidationException(ValidationExceptionCode.INVALID_DB_CONNECTION);
        }
        LOG.log(Level.INFO, "Connection to db is initialized.");
    }

    /**
   * Factory method for getting singleton.
   * @return connection instance
   */
    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }

    /**
   * Get database connection.
   * @return database connection
   */
    public Connection getConnection() {
        return con;
    }

    /**
   * Close connection.
   */
    public void close() {
        try {
            Statement s = null;
            try {
                s = con.createStatement();
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, "Problem with creating statement.", e);
            }
            if (s != null) {
                s.execute("SHUTDOWN");
            }
            con.close();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Problem with loading jdbc driver.", e);
        }
    }
}
