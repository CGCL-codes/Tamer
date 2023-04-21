package model;

import java.util.Vector;

/**
 * 
 * @author christian antic <e0525482[at]student.tuwien.ac.at>
 */
public class AgentGroup {

    private String name;

    private Vector<Agent> agents;

    public AgentGroup(String name, Vector<Agent> agents) {
        this.name = name;
        this.agents = agents;
    }

    public String getName() {
        return name;
    }

    public Vector<Agent> getAgents() {
        return agents;
    }

    @Override
    public String toString() {
        return getName();
    }
}
