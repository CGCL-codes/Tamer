package com.google.gwt.dev.jjs.impl.gflow.liveness;

import com.google.gwt.dev.jjs.impl.gflow.Analysis;
import com.google.gwt.dev.jjs.impl.gflow.AssumptionMap;
import com.google.gwt.dev.jjs.impl.gflow.FlowFunction;
import com.google.gwt.dev.jjs.impl.gflow.IntegratedAnalysis;
import com.google.gwt.dev.jjs.impl.gflow.IntegratedFlowFunction;
import com.google.gwt.dev.jjs.impl.gflow.cfg.Cfg;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgEdge;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgNode;
import com.google.gwt.dev.jjs.impl.gflow.cfg.CfgTransformer;

/**
 * Analysis which detects when variable is not used after the assignment,
 * and eliminates assignment.
 */
public class LivenessAnalysis implements Analysis<CfgNode<?>, CfgEdge, Cfg, LivenessAssumption>, IntegratedAnalysis<CfgNode<?>, CfgEdge, CfgTransformer, Cfg, LivenessAssumption> {

    private static final LivenessFlowFunction FLOW_FUNCTION = new LivenessFlowFunction();

    private static final LivenessIntegratedFlowFunction INTEGRATED_FLOW_FUNCTION = new LivenessIntegratedFlowFunction();

    public FlowFunction<CfgNode<?>, CfgEdge, Cfg, LivenessAssumption> getFlowFunction() {
        return FLOW_FUNCTION;
    }

    public IntegratedFlowFunction<CfgNode<?>, CfgEdge, CfgTransformer, Cfg, LivenessAssumption> getIntegratedFlowFunction() {
        return INTEGRATED_FLOW_FUNCTION;
    }

    public void setInitialGraphAssumptions(Cfg graph, AssumptionMap<CfgEdge, LivenessAssumption> assumptionMap) {
    }
}
