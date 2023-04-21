package fr.cnes.sitools.dataset.database;

import java.util.List;
import fr.cnes.sitools.common.exception.SitoolsException;
import fr.cnes.sitools.datasource.jdbc.model.Record;

/**
 * Interface for database request
 * 
 * @author m.gond (AKKA Technologies)
 */
public interface DatabaseRequest {

    /**
   * Get the list of primary keys in the DataSet
   * 
   * @return The list of primary keys
   */
    List<String> getPrimaryKeys();

    /**
   * Get the selected primary keys
   * 
   * @return a list of strings containing primary keys
   */
    List<String> getSelectedPrimaryKey();

    /**
   * Create a SQL Distinct request
   * 
   * @throws SitoolsException
   *           when occurs
   */
    void createDistinctRequest() throws SitoolsException;

    /**
   * 
   * Create a Request
   * 
   * 
   * @throws SitoolsException
   *           when request fails
   */
    void createRequest() throws SitoolsException;

    /**
   * get the number of records in the current request
   * 
   * @return the number of records in the current request
   */
    int getTotalCount();

    /**
   * Get the number of results in the current request for pagination purpose
   * 
   * @return the number of results in the current request for pagination purpose
   */
    int getCount();

    /**
   * Get the index of the first result in the current request
   * 
   * @return the index of the first result in the current request
   */
    int getStartIndex();

    /**
   * Move the cursor on the next result.
   * 
   * @return <code>true</code> if the new current row is valid; <code>false</code> if there are no more rows
   * @throws SitoolsException
   *           if a database access error occurs or this method is called on a closed result set
   */
    boolean nextResult() throws SitoolsException;

    /**
   * Retrieves whether the cursor is on the last row of this <code>ResultSet</code> object or is the last
   * <code>ResultSet</code> to send.
   * 
   * @return <code>true</code> if the cursor is on the last row or is the last row to send.; <code>false</code>
   *         otherwise
   * @throws SitoolsException
   *           if a database access error occurs or this method is called on a closed result set
   * @deprecated
   */
    boolean isLastResult() throws SitoolsException;

    /**
   * Return the current <code>ResultSet</code> in a <code>Record</code> object
   * 
   * @return the <code>Record</code> of the current <code>ResultSet</code>
   * @throws SitoolsException
   *           if a database access error occurs or this method is called on a closed result set
   */
    Record getRecord() throws SitoolsException;

    /**
   * Close the connection
   * 
   * @throws SitoolsException
   *           if a database access error occurs or this method is called on a closed result set
   */
    void close() throws SitoolsException;

    /**
   * If the count must be done or not
   * 
   * @return true if the count must be done, false otherwise
   */
    boolean isCountDone();

    /**
   * Gets the distinct request as a String
   * 
   * @return the String of a Distinct request
   * @throws SitoolsException
   *           when occurs
   */
    String getDistinctRequestAsString() throws SitoolsException;

    /**
   * Gets the request as a String
   * 
   * @return the String of a Distinct request
   * @throws SitoolsException
   *           when occurs
   */
    String getRequestAsString() throws SitoolsException;

    /**
   * Get the maximal number of results in the current request
   * 
   * @return the maximal number of results in the current request
   */
    int getMaxResultsToSend();

    /**
   * Build the URI to the current Record
   * 
   * @return The URI to the current Record
   * @throws SitoolsException
   *           if a database access error occurs or this method is called on a closed result set
   */
    String buildURI() throws SitoolsException;
}
