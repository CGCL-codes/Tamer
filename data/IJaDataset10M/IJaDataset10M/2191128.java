package org.matsim.events.handler;

import org.matsim.events.EventAgentStuck;

public interface EventHandlerAgentStuckI extends EventHandlerI {

    public void handleEvent(EventAgentStuck event);
}
