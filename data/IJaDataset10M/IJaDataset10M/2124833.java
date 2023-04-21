package org.jdna.bmt.web.client.event;

import com.google.gwt.event.shared.HandlerManager;

public class EventBus {

    private static final HandlerManager eventBus = new HandlerManager(null);

    public static HandlerManager getHandlerManager() {
        return eventBus;
    }
}
