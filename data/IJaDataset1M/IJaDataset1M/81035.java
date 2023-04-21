package org.freelords.network.update;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.freelords.network.client.Session;

/** Returns the list of registered clients on the server.
 *
 * <p>
 * This update is sent whenever a player registers at the server or disconnects.
 * It is essentially just a list of player ids and associated names.
 * It also contains a "greeting", that is, a welcome or goodbye message 
 * announcing the new or disconnected client.
 * </p>
 */
public class ClientListUpdate implements Update {

    private final Map<String, String> clients = new HashMap<String, String>();

    /**
	 * Greeting -- welcome or goodbye message
	 * following format should be used
	 * GoodBye message: "Good-Bye --> ClientName"
	 * Welcome message: "Welcome --> ClientName" 
	 */
    private String greetMessage;

    /**
	 * Constants for the greeting messages
	 */
    public static final String WELCOME = "Welcome --> ";

    public static final String GOODBYE = "Good-Bye --> ";

    public ClientListUpdate() {
    }

    public ClientListUpdate(String greetMessage) {
        this.greetMessage = greetMessage;
    }

    /**
	 * Adds a client to the list of the clients playing or guesting the game.
	 *
	 * @param clientId The id of the new client
	 * @param clientName The name of the new client
	 */
    public void addClient(UUID clientId, String clientName) {
        this.clients.put(clientId.toString(), clientName);
    }

    /**
	 * This would update the Session object
	 *
	 * @param session The session to be updated with new data.
	 */
    @Override
    public void updateSession(Session session) {
        Map<UUID, String> transformedMap = new HashMap<UUID, String>();
        for (String key : clients.keySet()) {
            transformedMap.put(UUID.fromString(key), clients.get(key));
        }
        session.updateClients(transformedMap);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " clients:" + clients.toString();
    }

    public String getGreetMessage() {
        return this.greetMessage;
    }
}
