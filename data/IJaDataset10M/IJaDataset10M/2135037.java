package gumbo.wip.net;

import gumbo.net.NetBroker;

/**
 * Interface for listeners of network protocol events.
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 */
public interface NetBrokerListener {

    /**
	 * Called when the status of a NetProtocol changes.
	 * @param source Temp exposed protocol that was affected. Never null.
	 * @param type Event type. Never null.
	 * @param msg Message describing the event. Null if none.
	 * @param cause Throwable that caused the event. Null if none.
	 */
    public void handleNetBroker(NetBroker source, NetBroker.EventType type, String msg, Throwable cause);
}
