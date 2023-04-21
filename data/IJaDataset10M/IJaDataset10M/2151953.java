package com.dukesoftware.utils.agent.strategy;

import com.dukesoftware.utils.agent.Agent;

public interface IStrategy<T extends Agent> {

    void exec();
}
