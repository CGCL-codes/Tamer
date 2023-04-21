package com.agentfactory.agentspeak.debugger;

import com.agentfactory.platform.core.IAgent;
import com.agentfactory.visualiser.core.IStateManager;
import com.agentfactory.visualiser.core.IStateManagerFactory;

public class AgentSpeakStateManagerFactory implements IStateManagerFactory {

    private int maxHistory;

    public void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }

    @Override
    public IStateManager createStateManager(IAgent agent) {
        IStateManager manager = new AgentSpeakStateManager();
        manager.setMaxHistory(maxHistory);
        manager.setAgent(agent);
        return manager;
    }

    @Override
    public String getType() {
        return "AgentSpeak";
    }
}
