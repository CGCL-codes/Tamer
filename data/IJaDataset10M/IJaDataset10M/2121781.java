package org.eucontract.agents.knowledge.action;

import org.eucontract.agents.engine.AgentKernel;

public interface Action {

    void execute(AgentKernel messagePool);
}
