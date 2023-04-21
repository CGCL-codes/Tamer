package org.matsim.events.handler;

import org.matsim.events.AgentReplanEvent;

/**
 * @author dgrether
 *
 */
public interface EventHandlerAgentReplan extends EventHandlerI {

    public void handleEvent(AgentReplanEvent event);
}
