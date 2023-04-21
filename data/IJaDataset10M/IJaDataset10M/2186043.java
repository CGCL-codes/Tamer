package org.guicebox.failover;

/**
 * Listener interface for {@link Heart}.
 * 
 * @author willhains
 */
public interface HeartbeatListener {

    /**
	 * Called when each heartbeat is received.
	 * 
	 * @param heartbeat the heartbeat of the sending node.
	 */
    void onHeartbeat(Heartbeat heartbeat);

    /**
	 * Called when a heartbeat has not been received within the configured timeout.
	 */
    void onHeartbeatTimeout();
}
