package org.jage.monitor;

import org.jage.event.AbstractEvent;

/**
 * The base interface for all monitors. It enables a monitored object to inform
 * an external object about its deletion.
 * 
 * Note: in order to prevent deadlocks events of monitors can
 *   be invoked in a short time after the monitor was unregistered.
 *   
 * Note: monitors should not override equals and hashCode methods.
 * 
 * @author KrzS
 */
public interface IMonitor {

    /**
	 * The method executed if the owner of the monitor is being deleted.
	 * 
	 * @param event
	 *            the event that caused the deletion
	 */
    public void ownerDeleted(AbstractEvent event);
}
