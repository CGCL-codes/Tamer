package net.laubenberger.bogatyr.controller.net.client;

import net.laubenberger.bogatyr.misc.Event;
import net.laubenberger.bogatyr.misc.Listener;

/**
 * ListenerClient
 *
 * @author Stefan Laubenberger
 * @author Silvan Spross
 * @version 0.9.2 (20100520)
 * @since 0.7.0
 */
public interface ListenerClient extends Listener {

    /**
	 * Sends the read data from the client.
	 *
	 * @param event for the listener
	 * @since 0.7.0
	 */
    void clientStreamRead(Event<Client> event);

    /**
	 * Informs the listener that the client has started.
	 *
	 * @param event for the listener
	 * @since 0.7.0
	 */
    void clientStarted(Event<Client> event);

    /**
	 * Informs the listener that the client has stopped.
	 *
	 * @param event for the listener
	 * @since 0.7.0
	 */
    void clientStopped(Event<Client> event);
}
