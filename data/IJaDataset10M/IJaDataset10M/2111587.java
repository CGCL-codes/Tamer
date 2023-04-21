package org.matsim.core.events;

import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.events.BasicLinkLeaveEvent;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Person;

public class LinkLeaveEvent extends LinkEvent implements BasicLinkLeaveEvent {

    public static final String EVENT_TYPE = "left link";

    public LinkLeaveEvent(final double time, final Person agent, final Link link) {
        super(time, agent, link);
    }

    public LinkLeaveEvent(final double time, final Id agentId, final Id linkId) {
        super(time, agentId, linkId);
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public String getTextRepresentation() {
        return asString() + "2\t" + EVENT_TYPE;
    }
}
