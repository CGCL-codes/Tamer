package org.freelords.network.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Session {

    private Map<UUID, String> clientMapping = new HashMap<UUID, String>();

    private UUID curClientId;

    /** Creates a new empty Session object. */
    public Session() {
    }

    /** Updates the internal list of clients.
	 *
	 * @param clients the new mapping from client id's to client names.
	 */
    public synchronized void updateClients(Map<UUID, String> newClients) {
        clientMapping = new HashMap<UUID, String>(newClients);
    }

    /** Returns a set of the id's of all registered clients. */
    public synchronized Set<UUID> listClientIds() {
        return Collections.unmodifiableSet(clientMapping.keySet());
    }

    /** Returns the name of a client with a specific id. */
    public synchronized String getClientName(UUID id) {
        return clientMapping.get(id);
    }

    /** Sets the id of the client that holds the session object. */
    public synchronized void setCurrentClientId(UUID id) {
        curClientId = id;
    }

    /** Returns the id of the client that holds this session object. */
    public synchronized UUID getCurrentClientId() {
        return curClientId;
    }

    public synchronized boolean isListClientsIdEmpty() {
        return clientMapping.isEmpty();
    }
}
