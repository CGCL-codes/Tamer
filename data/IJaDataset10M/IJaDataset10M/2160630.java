package org.matsim.core.events;

import java.util.Map;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.events.BasicAgentEvent;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.population.Leg;
import org.matsim.core.api.population.Person;

public abstract class AgentEvent extends PersonEvent implements BasicAgentEvent {

    public static final String ATTRIBUTE_LINK = "link";

    private Leg leg;

    private final Id linkId;

    AgentEvent(final double time, final Person agent, final Link link, final Leg leg) {
        super(time, agent);
        this.linkId = link.getId();
        this.leg = leg;
    }

    AgentEvent(final double time, final Id agentId, final Id linkId) {
        super(time, agentId);
        this.linkId = linkId;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_LINK, this.linkId.toString());
        return attr;
    }

    protected String asString() {
        return getTimeString(this.getTime()) + this.getPersonId() + "\t\t" + this.getLinkId().toString() + "\t0\t";
    }

    public Leg getLeg() {
        return this.leg;
    }

    public Id getLinkId() {
        return this.linkId;
    }
}
