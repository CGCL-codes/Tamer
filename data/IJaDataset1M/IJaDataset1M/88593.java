package com.agileplex.workflow.stateflow;

import java.util.Collection;

public interface StateflowNode extends StateflowPart {

    StateflowArc getIncomingArc(String id);

    Collection<? extends StateflowArc> getIncomingArcs();

    StateflowArc getOutgoingArc(String id);

    Collection<? extends StateflowArc> getOutgoingArcs();
}
