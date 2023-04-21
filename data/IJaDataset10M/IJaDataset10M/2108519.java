package com.agileplex.workflow.stateflow;

public interface StateflowPlaceToTransitionArc extends StateflowArc {

    StateflowPlace getSource();

    StateflowTransition getTarget();
}
