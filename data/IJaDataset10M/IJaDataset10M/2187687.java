package com.agileplex.workflow.stateflow;

import java.util.Collection;

public interface StateflowTransition extends StateflowNode {

    StateflowPlaceToTransitionArc getIncomingArc(String id);

    Collection<StateflowPlaceToTransitionArc> getIncomingArcs();

    StateflowTransitionToPlaceArc getOutgoingArc(String id);

    Collection<StateflowTransitionToPlaceArc> getOutgoingArcs();

    StateflowTransitionContext createContext();

    Checkable getLabel();
}
