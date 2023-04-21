package de.tud.kom.nat.comm;

import java.nio.channels.SelectableChannel;

/**
 * This interface declares functions to limit the bandwidth.
 *
 * @author Matthias Weinert
 */
public interface IBandwidthController {

    /**
	 * Limits the outgoing traffic of the given <tt>SelectableChannel</tt> to the specified
	 * <tt>bytesPerSec</tt>.
	 * @param channel channel
	 * @param bytesPerSec the allowed outgoing traffic
	 */
    public abstract void limitOutgoingTraffic(SelectableChannel channel, int bytesPerSec);

    /**
	 * Limits the incoming traffic of the given <tt>SelectableChannel</tt> to the specified
	 * <tt>bytesPerSec</tt>.
	 * @param channel channel
	 * @param bytesPerSec the allowed incoming traffic
	 */
    public abstract void limitIncomingTraffic(SelectableChannel channel, int bytesPerSec);
}
