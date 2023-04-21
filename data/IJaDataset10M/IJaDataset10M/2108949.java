package com.ibm.wala.demandpa.flowgraph;

import com.ibm.wala.ipa.callgraph.propagation.cfa.CallerSiteContext;

public class ParamLabel extends CallLabel {

    private ParamLabel(CallerSiteContext callSite) {
        super(callSite);
    }

    public static ParamLabel make(CallerSiteContext callSite) {
        return new ParamLabel(callSite);
    }

    public void visit(IFlowLabelVisitor v, Object dst) throws IllegalArgumentException {
        if (v == null) {
            throw new IllegalArgumentException("v == null");
        }
        v.visitParam(this, dst);
    }

    @Override
    public String toString() {
        return "param[" + callSite + "]";
    }

    public ParamBarLabel bar() {
        return ParamBarLabel.make(callSite);
    }

    public boolean isBarred() {
        return false;
    }
}
