package com.ixora.rms.client.model;

/**
 * @author Daniel Moraru
 */
final class ResourcePath {

    private HostNode host;

    private AgentNode agent;

    private EntityNode entity;

    private CounterInfo counter;

    /**
     * Constructor.
     * @param hn
     * @param an
     * @param en
     * @param ci
     */
    public ResourcePath(HostNode hn, AgentNode an, EntityNode en, CounterInfo ci) {
        super();
        this.agent = an;
        this.host = hn;
        this.entity = en;
        this.counter = ci;
    }

    /**
     * @return the agent.
     */
    public AgentNode getAgent() {
        return agent;
    }

    /**
     * @return the entity.
     */
    public EntityNode getEntity() {
        return entity;
    }

    /**
     * @return the host.
     */
    public HostNode getHost() {
        return host;
    }

    /**
     * @return the counter
     */
    public CounterInfo getCounter() {
        return counter;
    }
}
