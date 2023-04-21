package de.dgrid.bisgrid.services.workflow.factory.strategies;

import de.dgrid.bisgrid.common.bpel.BPELWorkflowEngine;
import de.dgrid.bisgrid.common.bpel.BPELWorkflowEngineManager;

public class GetBpelEngineByRoundRobinStrategy implements GetBpelEngineStrategy {

    private final BPELWorkflowEngineManager manager;

    private int nextEngineId = 0;

    public GetBpelEngineByRoundRobinStrategy(BPELWorkflowEngineManager manager) {
        this.manager = manager;
    }

    @Override
    public BPELWorkflowEngine getBpelWorkflowEngine() {
        BPELWorkflowEngine result = manager.getEngineById(nextEngineId);
        if (nextEngineId++ >= manager.size()) nextEngineId = 0;
        return result;
    }
}
