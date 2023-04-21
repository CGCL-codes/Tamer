package org.mobicents.media.server.spi.listener;

/**
 * Generic event interface.
 * 
 * @author kulikov
 */
public interface Event<S> {

    public S getSource();
}
