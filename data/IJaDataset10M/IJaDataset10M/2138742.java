package org.matsim.events;

import org.matsim.network.Link;
import org.matsim.network.NetworkLayer;
import org.matsim.plans.Leg;
import org.matsim.plans.Person;
import org.matsim.plans.Plans;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class EventAgentArrival extends AgentEvent {

    private static final long serialVersionUID = -975513797122449530L;

    public EventAgentArrival(double time, String agentId, int legId, String linkId, Person agent, Leg leg, Link link) {
        super(time, agentId, legId, linkId, agent, leg, link);
    }

    public EventAgentArrival(double time, String agentId, int legId, String linkId) {
        super(time, agentId, legId, linkId);
    }

    @Override
    public Attributes getAttributes() {
        AttributesImpl impl = getAttributesImpl();
        impl.addAttribute("", "", "type", "", "arrival");
        return impl;
    }

    @Override
    public String toString() {
        return asString() + "0\tarrival";
    }

    @Override
    public void rebuild(Plans population, NetworkLayer network) {
        rebuildAgentData(population, network);
    }
}
