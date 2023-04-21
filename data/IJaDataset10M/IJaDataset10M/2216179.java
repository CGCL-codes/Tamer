package org.rascalli.framework.agentManager.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.rascalli.framework.core.Agent;

public class AgentStore {

    private final Map<Integer, Agent> agents = new HashMap<Integer, Agent>();

    private final DoubleKeyMap<String, Integer, Agent> agentsByFactory = new DoubleKeyMap<String, Integer, Agent>();

    private final DoubleKeyMap<Integer, Integer, Agent> agentsByUser = new DoubleKeyMap<Integer, Integer, Agent>();

    public void add(Agent agent) {
        agents.put(agent.getId(), agent);
        agentsByFactory.put(agent.getAgentFactoryId(), agent.getId(), agent);
        agentsByUser.put(agent.getUser().getId(), agent.getId(), agent);
    }

    public Agent getAgentById(int agentId) {
        return agents.get(agentId);
    }

    public Collection<Agent> getAgentsForFactory(String factoryId) {
        return agentsByFactory.values(factoryId);
    }

    public Collection<Agent> getAgentsForUser(int userId) {
        return agentsByUser.values(userId);
    }

    public Collection<Agent> getAllAgents() {
        return agents.values();
    }

    public void clear() {
        agents.clear();
        agentsByFactory.clear();
        agentsByUser.clear();
    }

    public Collection<Agent> removeAgentsForFactory(String factoryId) {
        Collection<Agent> removedAgents = agentsByFactory.removeAll(factoryId);
        for (Agent agent : removedAgents) {
            agents.remove(agent.getId());
            agentsByUser.remove(agent.getUser().getId(), agent.getId());
        }
        return removedAgents;
    }

    public Agent removeAgent(int agentId) {
        Agent agent = agents.remove(agentId);
        if (agent != null) {
            agentsByFactory.remove(agent.getAgentFactoryId(), agentId);
            agentsByUser.remove(agent.getUser().getId(), agentId);
        }
        return agent;
    }
}
