package org.jul.warp.impl;

import java.util.Map;
import org.jul.dsm.IgnoreField;
import org.jul.warp.*;

class ExecutableWrapperImpl implements ExecutableWrapper, Warp {

    private final Executable executable;

    @IgnoreField
    private AgentImpl agent;

    @IgnoreField
    private Node node;

    @IgnoreField
    private Map<AgentId, Executable> dependencies;

    public boolean execute() throws Exception {
        return executable.execute();
    }

    public boolean initialize() throws Exception {
        return executable.initialize(this);
    }

    public void setAgent(AgentImpl agent) {
        this.agent = agent;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setDependencies(Map<AgentId, Executable> dependencies) {
        this.dependencies = dependencies;
    }

    public Executable getExecutable() {
        return executable;
    }

    public AgentId getCurrentAgent() {
        return agent.getId();
    }

    public void addDependency(AgentId id) {
        agent.addDependency(id);
    }

    public void removeDependency(AgentId id) {
        agent.removeDependency(id);
    }

    public void addEquivalence(AgentId id) {
        agent.addEquivalence(id);
    }

    public void removeEquivalence(AgentId id) {
        agent.removeEquivalence(id);
    }

    public Id addAgent(Executable executable, AgentType type) {
        if (executable == null) {
            throw new NullPointerException();
        }
        return node.addAgent(executable, type).getId();
    }

    public void addAgentListener(Class<?> clazz) {
        agent.addAgentListener(clazz);
    }

    public void removeAgentListener(Class<?> clazz) {
        agent.removeAgentListener(clazz);
    }

    public Map<AgentId, Executable> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "Wrapper {" + agent + "}";
    }

    public ExecutableWrapperImpl(Executable executable) {
        this.executable = executable;
    }
}
