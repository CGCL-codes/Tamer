package ch.sahits.codegen.model;

import ch.sahits.model.db.IBasicDataBaseTable;
import ch.sahits.model.db.IBasicDatabaseTableBuilder;

/**
 * This interface defines the basic functionality that
 * is needed to generate a {@link IBasicDataBaseTable} from a 
 * connection.
 * This interface should not be implemented directly but through subinterfaces
 * that add an appropriate init method.
 * @author Andi Hotz
 * @since 0.9.0
 */
public interface IBasicDBConnectionModelGenerator {

    /**
	 * Test the connection to the database.
	 * @return Error message if the test failed or null
	 */
    public String testConnection();

    /**
	 * Generate the model from the connection
	 * @return generated model
	 */
    public IBasicDatabaseTableBuilder generateModel();

    /**
	 * Set the name of the database host
	 * @param host name of the host
	 */
    public void setHost(String host);

    /**
	 * Set the port number of the database
	 * @param port number
	 */
    public void setPort(int port);

    /**
	 * Set the name of the database user
	 * @param userName name of the user
	 */
    public void setUserName(String userName);

    /**
	 * Set the password for the database user
	 * @param pwd password
	 */
    public void setPassword(String pwd);

    /**
	 * Set the name of the database
	 * @param dbName database name
	 */
    public void setDatabase(String dbName);

    /**
	 * Set the name of the table
	 * @param tableName table name
	 */
    public void setTableName(String tableName);

    /**
	 * Retrieve the default name of the database host
	 * @return host name
	 */
    public String getDefaultHost();

    /**
	 * Retrieve the default port number of the data base
	 * @return port number
	 */
    public String getDefaultPort();

    /**
	 * Retrieve the default user name for the data base
	 * @return user name
	 */
    public String getDefaultUserName();

    /**
	 * This method rates the implementation. Since this
	 * Interface is used for an extension point Implementations with
	 * the highest ranking are choosen
	 * @return Ranking for the implementation
	 */
    public int getRating();

    /**
	 * Defines how complex the generation of the model
	 * is
	 * @return Number of work steps
	 */
    public int getWorkload();

    /**
	 * Indicates if this database works through schemas
	 * @return true if the schema must be supplied.
	 */
    public boolean hasSchema();

    /**
	 * Retrieve the connection String for the specific database
	 * @return connection string
	 * @since 1.2.0
	 */
    public String getConnectionString();

    /**
	 * Retrieve the driver class name for the JDBC connection
	 * @return fully qualified driver class name
	 * @since 1.2.0
	 */
    public String getConnectionDriverClass();
}
