package fr.n7.sma.model.listeners;

import java.util.EventListener;
import fr.n7.sma.model.listeners.events.AgentEvent;

public interface AgentListener<AgentProperty, CellAdditionnalProperty> extends EventListener {

    void agentChanged(AgentEvent<AgentProperty, CellAdditionnalProperty> event);
}
