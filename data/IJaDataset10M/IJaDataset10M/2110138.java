package net.stogi.common.gwt.frame.client.core.impl;

import net.stogi.common.gwt.frame.client.core.EventBus;
import com.google.gwt.event.shared.HandlerManager;

public class EventBusImpl extends HandlerManager implements EventBus {

    public EventBusImpl() {
        super(null);
    }
}
