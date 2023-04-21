package org.datanucleus.store.rdbms;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Connects to a DataSource to obtain a Connection.
 * The ConnectionProvider is not a caching and neither connection pooling mechanism.
 * The ConnectionProvider exists to perform failover algorithm on multiple DataSources when necessary.
 * One instance per StoreManager (RDBMSManager) is created.
 *
 * Users can provide their own implementation via the extension 
 * "org.datanucleus.store.rdbms.connectionprovider"
 */
public interface ConnectionProvider {

    /**
     * Flag if an error causes the operation to thrown an exception, or false to skip to next DataSource. 
     * If an error occurs on the last DataSource on the list an Exception will be thrown no matter if 
     * failOnError is true or false. This is a hint. Implementations may ignore the user setting and force 
     * it's own behaviour
     * @param flag true if to fail on error
     */
    void setFailOnError(boolean flag);

    /**
     * Obtain a connection from the datasources, starting on the first datasource, and if unable to obtain 
     * a connection skips to the next one on the list, and try again until the list is exhausted.
     * @param ds the array of datasources. An ordered list of datasources
     * @return the Connection, null if <code>ds</code> is null, or null if the DataSources has returned 
     *     a null as connection
     * @throws SQLException in case of error and failOnError is true or the error occurs while obtaining 
     *     a connection with the last DataSource on the list
     */
    Connection getConnection(DataSource[] ds) throws SQLException;
}
