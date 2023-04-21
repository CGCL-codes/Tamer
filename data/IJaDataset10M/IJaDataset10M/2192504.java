package net.sf.javadc.listeners;

import net.sf.javadc.interfaces.IClient;
import net.sf.javadc.net.client.Client;

/**
 * <code>ClientManagerListenerBase</code> is the default implementation of the <code>ClientManagerListener</code>
 * interface. It can be used as the super class for implementations which are only interested in few notifications.
 * 
 * @author Timo Westk�mper
 */
public class ClientManagerListenerBase implements ClientManagerListener {

    public void clientAdded(Client client) {
    }

    public void clientRemoved(IClient client) {
    }
}
