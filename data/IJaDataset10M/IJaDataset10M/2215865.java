package org.matsim.core.mobsim.queuesim;

import org.matsim.core.api.population.Person;

/**
 * @author dgrether
 */
public class AgentFactory {

    protected final QueueSimulation simulation;

    public AgentFactory(final QueueSimulation simulation) {
        this.simulation = simulation;
    }

    public PersonAgent createPersonAgent(final Person p) {
        PersonAgent agent = new PersonAgent(p, this.simulation);
        return agent;
    }
}
