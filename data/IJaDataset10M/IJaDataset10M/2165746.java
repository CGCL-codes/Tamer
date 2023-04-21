package com.googlecode.acpj.channels;

import java.util.Iterator;

/**
 * <p>
 * This interface provides a read-only view of the relevant properties of the
 * {@link com.googlecode.acpj.channels.Channel} and
 * {@link com.googlecode.acpj.channels.BufferedChannel} interfaces.
 * </p>
 * <p>
 * This interface is not related to the standard 
 * {@link com.googlecode.acpj.channels.Channel} interface by type, this
 * stops the channel monitor being used to bypass channel/port security.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface MonitoredChannel {

    /**
	 * Safe proxy for the method
	 * {@link com.googlecode.acpj.channels.Channel#getName()}
	 */
    public String getName();

    /**
	 * Return an iterator over all the read ports owned by this channel,
	 * note that this returns the safe {@link MonitoredPort} objects not
	 * any {@link com.googlecode.acpj.channels.Port} directly.
	 * 
	 * @return an iterator of {@link MonitoredPort} instances.
	 */
    public Iterator<MonitoredPort> getReadPorts();

    /**
	 * Safe proxy for the method
	 * {@link com.googlecode.acpj.channels.Channel#getReadPortArity()}
	 */
    public PortArity getReadPortArity();

    /**
	 * Return an iterator over all the write ports owned by this channel,
	 * note that this returns the safe {@link MonitoredPort} objects not
	 * any {@link com.googlecode.acpj.channels.Port} directly.
	 * 
	 * @return an iterator of {@link MonitoredPort} instances.
	 */
    public Iterator<MonitoredPort> getWritePorts();

    /**
	 * Safe proxy for the method
	 * {@link com.googlecode.acpj.channels.Channel#getWritePortArity()}
	 */
    public PortArity getWritePortArity();

    /**
	 * Return whether the current channel is an implementation of the 
	 * {@link com.googlecode.acpj.channels.BufferedChannel} and therefore
	 * has a buffer capacity.
	 * 
	 * @return <code>true</code> if this is a buffered channel.
	 */
    public boolean isBuffered();

    /**
	 * Safe proxy for the method
	 * {@link com.googlecode.acpj.channels.BufferedChannel#getBufferCapacity()}
	 */
    public int getBufferCapacity();

    /**
	 * Safe proxy for the method
	 * {@link com.googlecode.acpj.channels.BufferedChannel#size()}
	 */
    public int size();

    /**
	 * Safe proxy for the method
	 * {@link com.googlecode.acpj.channels.Poisonable#isPoisoned()}
	 */
    public boolean isPoisoned();
}
