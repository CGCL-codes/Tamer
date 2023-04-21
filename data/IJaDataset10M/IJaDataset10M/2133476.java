package org.matsim.core.events;

import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.events.BasicAgentWait2LinkEvent;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.Person;

public class AgentWait2LinkEvent extends AgentEvent implements BasicAgentWait2LinkEvent {

    public static final String EVENT_TYPE = "wait2link";

    public AgentWait2LinkEvent(final double time, final Person agent, final Link link, final Leg leg) {
        super(time, agent, link, leg);
    }

    public AgentWait2LinkEvent(final double time, final Id agentId, final Id linkId) {
        super(time, agentId, linkId);
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public String getTextRepresentation() {
        return asString() + "4\t" + EVENT_TYPE;
    }
}
