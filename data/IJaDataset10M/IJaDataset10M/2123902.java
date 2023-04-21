package net.sourceforge.jabm.mixing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.jabm.Population;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.agent.Agent;
import net.sourceforge.jabm.agent.AgentList;
import cern.jet.random.engine.RandomEngine;

public class SubgroupAgentMixer implements AgentMixer, Serializable {

    protected int groupSize;

    protected RandomEngine prng;

    protected int numGroups;

    public SubgroupAgentMixer(RandomEngine prng) {
        this.prng = prng;
    }

    public void invokeAgentInteractions(Population population, SimulationController simulation) {
        StochasticPairwiseAgentMixer pairwiseMixer = new StochasticPairwiseAgentMixer(prng);
        Collection<AgentList> groups = createGroups(population);
        Iterator<AgentList> it = groups.iterator();
        while (it.hasNext()) {
            AgentList group = it.next();
            pairwiseMixer.invokeInteractions(group, simulation);
        }
    }

    public Collection<AgentList> createGroups(Population population) {
        Collection<AgentList> groups = new ArrayList<AgentList>(numGroups);
        AgentList currentGeneration = population.getAgentList();
        currentGeneration.shuffle(prng);
        Iterator<Agent> it = currentGeneration.iterator();
        for (int i = 0; i < numGroups; i++) {
            AgentList group = new AgentList(groupSize);
            for (int j = 0; j < groupSize; j++) {
                group.add(it.next());
            }
            groups.add(group);
        }
        return groups;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getNumGroups() {
        return numGroups;
    }

    public void setNumGroups(int numGroups) {
        this.numGroups = numGroups;
    }
}
