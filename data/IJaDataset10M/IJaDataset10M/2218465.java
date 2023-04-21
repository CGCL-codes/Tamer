package org.dmd.dmp.server.servlet.base;

import org.dmd.dmp.server.extended.DMPEvent;
import org.dmd.dmp.server.servlet.base.cache.CacheRegistration;

/**
 * The EventHandlerIF interface is implemented by entities that want to register
 * with an EventBusIF for DMPEvent notification.
 */
public interface EventHandlerIF {

    public CacheRegistration getCacheRegistration();

    public void handleEvent(DMPEvent event);
}
