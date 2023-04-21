package com.agentfactory.platform.service;

import com.agentfactory.platform.core.Agent;

/**
 * This file implements a specific security right for a specific agent.  The agent can be specified
 * either by a reference to the object that implements the agent, or by the name of the agent.
 *
 * The former option is provided to allow us to set default access rights for the system agents, while
 * the latter is used to specify access rights for application agents.
 *
 * Currently two types of access right are supported:
 * - ALLOW means that the agent can access the specified service
 * - DENY means that the agent cannot access the specified service
 *
 * @author  Rem Collier
 */
public class AFRight {

    public static final int ALLOW = 0;

    public static final int DENY = 1;

    private int policy;

    private Agent agent;

    private String name;

    /**
     * Creates a new instance of Policy
     *
     * @param agent the agent to whom the policy applies
     * @param policy the right associated with the policy
     */
    public AFRight(Agent agent, int policy) {
        this.agent = agent;
        this.policy = policy;
    }

    /**
     * Creates a new instance of Policy
     *
     * @param name the name of the agent to whom the policy applies
     * @param policy the right associated with the policy
     */
    public AFRight(String name, int policy) {
        this.name = name;
        this.policy = policy;
    }

    public Agent getAgent() {
        return agent;
    }

    public String getAgentName() {
        if (agent == null) {
            return name;
        }
        return agent.getName();
    }

    public boolean isAllow() {
        return policy == ALLOW;
    }

    public boolean isDeny() {
        return policy == DENY;
    }
}
