package uk.co.lakesidetech.springxmldb.dao;

import java.util.Map;
import uk.co.lakesidetech.springxmldb.exception.XMLDBDataAccessException;

/**
 * Defines a Dao for querying a collection in a XML database with a xpath query
 * 
 * @author Stuart Eccles
 */
public interface IXPathXMLDBDao {

    /**
     * Query a collection with xpath and return a map of results with keys of
     * the resource IDs and values of w3c DOM documents of the results (not necessarily the resource
     * documents, but the result of the query)
     * @param xPathQuery the xpath query to use
     * @param collectionPath the collection path to query (note some XML databases will also 
     * query child collections by default)
     * @return a Map of the resourceIDs and query results
     * @throws XMLDBDataAccessException if anything goes wrong with he query
     */
    public abstract Map queryWithXPathCollectionAsDOM(String xPathQuery, String collectionPath) throws XMLDBDataAccessException;

    /**
     * Query a collection with xpath and return a map of results with keys of
     * the resource IDs and values of Strings of the results (not necessarily the resource
     * documents, but the result of the query)
     * @param xPathQuery the xpath query to use
     * @param collectionPath the collection path to query (note some XML databases will also 
     * query child collections by default)
     * @return a Map of the resourceIDs and query results
     * @throws XMLDBDataAccessException if anything goes wrong with he query
     */
    public abstract Map queryWithXPathCollectionAsString(String xPathQuery, String collectionPath) throws XMLDBDataAccessException;

    /**
     * Query a particular with xpath and return a map of results with keys of
     * the resource IDs and values of w3c DOM documents of the results (not necessarily the resource
     * documents, but the result of the query)
     * @param resourceId the resource to query
     * @param xPathQuery the xpath query to use
     * @param collectionPath the collection path to find the resource
     * @return a Map of the resourceIDs and query results
     * @throws XMLDBDataAccessException if anything goes wrong with he query
     */
    public abstract Map queryResourceWithXPathCollectionAsDOM(String resourceId, String xPathQuery, String collectionPath) throws XMLDBDataAccessException;

    /**
     * Query a particular with xpath and return a map of results with keys of
     * the resource IDs and values of String xml of the results (not necessarily the resource
     * documents, but the result of the query)
     * @param resourceId the resource to query
     * @param xPathQuery the xpath query to use
     * @param collectionPath the collection path to find the resource
     * @return a Map of the resourceIDs and query results
     * @throws XMLDBDataAccessException if anything goes wrong with he query
     */
    public abstract Map queryResourceWithXPathCollectionAsString(String resourceId, String xPathQuery, String collectionPath) throws XMLDBDataAccessException;
}
