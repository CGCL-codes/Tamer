package com.hongbo.cobweb.nmr.api;

import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import com.hongbo.cobweb.nmr.api.service.ServiceRegistry;

/**
 * The Registry is used to register endpoints, unregister them, query endpoints
 * and create a Channel to interfact with them.
 *
 * @version $Revision: $
 */
public interface EndpointRegistry extends ServiceRegistry<Endpoint> {

    /**
     * Register the given endpoint in the registry.
     * In an OSGi world, this would be performed automatically by a ServiceTracker.
     * Upon registration, a {@link Channel} will be injected onto the Endpoint using
     * the {@link Endpoint#setChannel(Channel)} method.
     *
     * @param endpoint the endpoint to register
     * @param properties the metadata associated with this endpoint
     */
    void register(Endpoint endpoint, Map<String, ?> properties);

    /**
     * Unregister a previously register enpoint.
     * In an OSGi world, this would be performed automatically by a ServiceTracker.
     *
     * @param endpoint the endpoint to unregister
     * @param properties the metadata associated with this endpoint
     */
    void unregister(Endpoint endpoint, Map<String, ?> properties);

    /**
     * Query the registry for a list of registered endpoints.
     *
     * @param properties filtering data
     * @return the list of endpoints matching the filters
     */
    List<Endpoint> query(Map<String, ?> properties);

    /**
     * From a given amount of metadata which could include interface name, service name
     * policy data and so forth, choose an available endpoint reference to use
     * for invocations.
     *
     * This could return actual endpoints, or a dynamic proxy to a number of endpoints
     *
     * @param properties filtering data
     */
    Reference lookup(Map<String, ?> properties);

    /**
     * This methods creates a Reference from its xml representation.
     *
     * @see Reference#toXml()
     * @param xml the xml document describing this reference
     * @return a new Reference
     */
    Reference lookup(Document xml);

    /**
     * Creates a Reference that select endpoints that match the
     * given LDAP filter.
     *
     * @param filter a LDAP filter used to find matching endpoints
     * @return a new Reference that uses the given filter
     */
    Reference lookup(String filter);
}
