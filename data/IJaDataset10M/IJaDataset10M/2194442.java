package org.osmius.service;

import org.osmius.model.OsmClient;
import org.osmius.service.exceptions.OsmClientExistsException;
import java.util.List;

/**
 * Business Service Interface to handle communication between web and persistence layer.
 * Exposes the neccessary methods to handle a client manager
 */
public interface OsmClientManager extends Manager {

    /**
    * Gets the clients from the database
    *
    * @param osmClient The client to be retrieve, can be null
    * @return A list with the clients
    */
    public List getOsmClients(OsmClient osmClient);

    /**
    * Gets a client from the database
    *
    * @param idnClient The Client ID
    * @return The client information
    */
    public OsmClient getOsmClient(final String idnClient);

    /**
    * Gets the clients that the user is authorized
    * @return The clients
    */
    public List getRestrictedOsmClients();

    /**
    * Delete clients
    * @param osmClients An array of clients
    * @return
    */
    public String[] removeOsmClients(String[] osmClients);

    /**
    * Saves clients
    * @param osmClient An array of clients
    * @throws OsmClientExistsException Throws a client exist exception in case the client already exist
    */
    public void saveOsmClient(OsmClient osmClient) throws OsmClientExistsException;

    /**
    * Updates a client
    * @param osmClient Client to be updated
    */
    public void updateOsmClient(OsmClient osmClient);
}
