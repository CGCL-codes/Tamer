package org.mobicents.media.server;

import java.util.Hashtable;
import org.mobicents.media.server.spi.ConnectionState;

/**
 * 
 * @author amit bhayani
 *
 */
public class ConnectionStateManager {

    private Hashtable<ConnectionState, Integer> connStateLifeTime;

    public ConnectionStateManager(Hashtable<ConnectionState, Integer> connStateLifeTime) {
        this.connStateLifeTime = connStateLifeTime;
    }

    public Hashtable<ConnectionState, Integer> getConnStateLifeTime() {
        return connStateLifeTime;
    }
}
