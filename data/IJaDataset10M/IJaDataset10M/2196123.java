package simis;

public class Agent {

    protected String agentId;

    public Agent(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentId() {
        return agentId;
    }

    public boolean equals(Agent toCompare) {
        return this.agentId.equals(toCompare.agentId);
    }

    public String toString() {
        return this.agentId;
    }
}
