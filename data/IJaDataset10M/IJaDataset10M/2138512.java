package org.wiigee.event;

import java.util.EventListener;

/**
 * Implement this interface to get informed about events only
 * occuring if a Wii Motion Plus is attached. If implemented and added
 * as a Listener, the RotationSpeedEvents and RotationEvent would be
 * retrieved.
 *
 * @author Benjamin 'BePo' Poppinga
 */
public interface RotationListener extends EventListener {

    public abstract void rotationSpeedReceived(RotationSpeedEvent event);

    public abstract void rotationReceived(RotationEvent event);
}
