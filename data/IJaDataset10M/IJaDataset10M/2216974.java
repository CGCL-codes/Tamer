package net.zschech.gwt.comet.client;

import java.io.Serializable;
import java.util.List;

/**
 * Listens for events from a {@link CometClient}.
 * 
 * @author Richard Zschech
 */
public interface CometListener {

    /**
	 * The connection has been established
	 * @param heartbeat
	 */
    public void onConnected(int heartbeat);

    /**
	 * The connection has disconnected and is being refreshed
	 */
    public void onDisconnected();

    /**
	 * A Comet error has occurred
	 * @param exception
	 * @param connected
	 */
    public void onError(Throwable exception, boolean connected);

    /**
	 * The connection has received a heartbeat
	 */
    public void onHeartbeat();

    /**
	 * The connection should be refreshed by the client 
	 */
    public void onRefresh();

    /**
	 * A batch of messages has been received
	 * @param messages
	 */
    public void onMessage(List<? extends Serializable> messages);
}
