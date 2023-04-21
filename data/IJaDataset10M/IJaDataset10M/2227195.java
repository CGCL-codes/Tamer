package net.turingcomplete.phosphor.loadserver;

/**
 *
 * @author  Joshua Allen
 * @version 
 */
public interface ServerListenerCallback {

    public void serverRegistered(ServerListener.ServerConnection who);

    public void serverUnregistered(ServerListener.ServerConnection who);
}
