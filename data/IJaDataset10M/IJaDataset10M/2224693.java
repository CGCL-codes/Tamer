package net.sf.provisioner.connectors;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import net.sf.provisioner.config.DataBase;

/**
 * Esta clase .
 * <p>
 * 
 * .
 * <p>
 * 
 *             
 * @version $Revision: 1.1.2.1 $, $Date: 2007/11/12 01:57:26 $
 * @author Gonzalo Espert
 */
public class JDBCConnector extends Connector {

    String connectionString;

    DataBase db;

    public Connection connection = null;

    public Statement statement = null;

    /**
     * .
     * <p>
     * 
     * .
     * <p>
     * 
     * 
     * 
     * @param 
     *            
     * @throws 
     *            
     *           
     */
    public JDBCConnector(DataBase db) {
        this.db = db;
        if (!this.isConnected) {
            this.Connect();
            try {
                this.statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            } catch (SQLException se) {
                logger.fatal("We got an exception while creating a statement:" + "that probably means we're no longer connected.");
                se.printStackTrace();
            }
        }
    }

    /**
     * .
     * <p>
     * 
     * .
     * <p>
     * 
     * 
     * 
     * @param 
     *            
     * @throws 
     *            
     *           
     */
    public void Connect() {
        this.connectionString = this.db.driver.type + ":" + this.db.server.type + "://" + this.db.server.name;
        if (this.db.server.type.equalsIgnoreCase("sqlserver")) {
            this.connectionString = this.connectionString + ";databaseName=";
        } else {
            this.connectionString = this.connectionString + "/";
        }
        this.connectionString = this.connectionString + this.db.name;
        logger.debug("String de conexion: " + this.connectionString);
        try {
            Class.forName(this.db.driver.className);
        } catch (ClassNotFoundException cnfe) {
            logger.fatal("Couldn't find driver class");
            cnfe.printStackTrace();
        }
        try {
            this.connection = DriverManager.getConnection(this.connectionString, this.db.user, this.db.password);
            this.isConnected = true;
            logger.debug("Database connection attempt successful!");
        } catch (SQLException se) {
            logger.fatal("Couldn't establish database connection: " + se.getMessage());
            se.printStackTrace();
        }
    }

    /**
     * .
     * <p>
     * 
     * .
     * <p>
     * 
     * 
     * 
     * @param 
     *            
     * @throws 
     *            
     *           
     */
    public void Disconnect() {
        try {
            this.connection.close();
        } catch (SQLException se) {
            logger.fatal("Couldn't close connection: print out a stack trace and exit.");
            se.printStackTrace();
        }
    }
}
