package com.dbxml.db.client.xmldb;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;

/**
 * FullTextQueryService is a <code>Service</code> that enables the execution
 * of full text querying within the context of a <code>Collection</code>.
 */
public interface FullTextQueryService extends Service {

    /**
    * Sets a namespace mapping in the internal namespace map used to evaluate
    * queries. If <code>prefix</code> is null or empty the default namespace is
    * associated with the provided URI. A null or empty <code>uri</code> results
    * in an exception being thrown.
    *
    * @param prefix The prefix to set in the map. If
    *  <code>prefix</code> is empty or null the
    *  default namespace will be associated with the provided URI.
    * @param uri The URI for the namespace to be associated with prefix.
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
    * @todo probably need some special error here.
    */
    void setNamespace(String prefix, String uri) throws XMLDBException;

    /**
    * Returns the URI string associated with <code>prefix</code> from
    * the internal namespace map. If <code>prefix</code> is null or empty the
    * URI for the default namespace will be returned. If a mapping for the
    * <code>prefix</code> can not be found null is returned.
    *
    * @param prefix The prefix to retrieve from the namespace map.
    * @return The URI associated with <code>prefix</code>
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
    */
    String getNamespace(String prefix) throws XMLDBException;

    /**
    * Removes the namespace mapping associated with <code>prefix</code> from
    * the internal namespace map. If <code>prefix</code> is null or empty the
    * mapping for the default namespace will be removed.
    *
    * @param prefix The prefix to remove from the namespace map. If
    *  <code>prefix</code> is null or empty the mapping for the default
    *  namespace will be removed.
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
    */
    void removeNamespace(String prefix) throws XMLDBException;

    /**
    * Removes all namespace mappings stored in the internal namespace map.
    *
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
    */
    void clearNamespaces() throws XMLDBException;

    /**
    * Performs a full-text query against the <code>Collection</code>.
    * The result is a <code>ResourceSet</code> containing the results of the
    * query. Any namespaces used in the <code>query</code> string will be
    * evaluated using the mappings setup using <code>setNamespace</code>.
    *
    * @param query The query string to use.
    * @return A <code>ResourceSet</code> containing the results of the query.
    * @exception XMLDBException with expected error codes.<br />
    *  <code>ErrorCodes.VENDOR_ERROR</code> for any vendor
    *  specific errors that occur.<br />
    */
    ResourceSet query(String query) throws XMLDBException;
}
