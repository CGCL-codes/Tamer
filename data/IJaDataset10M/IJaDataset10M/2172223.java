package com.ivis.xprocess.ui.workflowdesigner.diagram.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.ivis.xprocess.core.State;
import com.ivis.xprocess.core.StateSet;
import com.ivis.xprocess.core.StateTransition;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.ui.workflowdesigner.preferences.Preferences;

public class ModelBuilder {

    public static Model build(StateSet stateSet) {
        Session session = new Session();
        session.build(stateSet);
        Model model = new ModelImpl(session.getRoot(), session.getMap());
        return model;
    }

    private static class Session {

        private Start myStart;

        private End myEnd;

        private Node myRootNode;

        private Map<Xelement, Element> myX2Model;

        public void build(StateSet stateSet) {
            myX2Model = new HashMap<Xelement, Element>();
            final StateSetImpl modelStateSet = new StateSetImpl(stateSet.getUuid());
            myRootNode = modelStateSet;
            List<Node> modelStates = new ArrayList<Node>();
            Collection<Node> modelTriggers = new ArrayList<Node>();
            Collection<Node> modelTriggersAnyState = new ArrayList<Node>();
            if (stateSet.getTheNonExistentState() != null) {
                myStart = new StartImpl(stateSet.getTheNonExistentState().getUuid());
                myX2Model.put(stateSet.getTheNonExistentState(), myStart);
                myEnd = new EndImpl(stateSet.getTheNonExistentState().getUuid());
            }
            for (State state : stateSet.getStatesInOrder()) {
                if (state.equals(stateSet.getTheNonExistentState())) {
                    continue;
                }
                com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelState = new StateImpl(state.getUuid());
                myX2Model.put(state, modelState);
                modelStates.add(modelState);
            }
            if (!Preferences.getHideExpectedTransitionLinks()) {
                for (State state : stateSet.getStatesInOrder()) {
                    com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelState = getModelState(state, stateSet, true);
                    for (State expectedTransitionState : state.getExpectedTransitions()) {
                        com.ivis.xprocess.ui.workflowdesigner.diagram.model.State expectedTransitionModelState = getModelState(expectedTransitionState, stateSet, false);
                        createLink(modelState, expectedTransitionModelState);
                    }
                }
            }
            AnyStateImpl anyState = new AnyStateImpl();
            for (StateTransition transition : stateSet.getStateTransitions()) {
                Set<State> preStates = transition.getPreStates();
                Set<State> postStates = transition.getPostStates();
                if (preStates.isEmpty()) {
                    if (!postStates.isEmpty()) {
                        TransitionTrigger transitionTrigger = new TransitionTriggerImpl(transition.getUuid(), false);
                        myX2Model.put(transition, transitionTrigger);
                        modelTriggers.add(transitionTrigger);
                        for (State postState : postStates) {
                            com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelPostState = getModelState(postState, stateSet, false);
                            createLink(anyState, transitionTrigger);
                            createLink(transitionTrigger, modelPostState);
                        }
                    } else {
                        TransitionTrigger transitionTrigger = new TransitionTriggerImpl(transition.getUuid(), false);
                        myX2Model.put(transition, transitionTrigger);
                        modelTriggersAnyState.add(transitionTrigger);
                        createLink(anyState, transitionTrigger);
                        createLink(transitionTrigger, anyState);
                    }
                } else if (postStates.isEmpty()) {
                    TransitionTrigger transitionTrigger = new TransitionTriggerImpl(transition.getUuid(), false);
                    myX2Model.put(transition, transitionTrigger);
                    modelTriggers.add(transitionTrigger);
                    for (State preState : preStates) {
                        com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelPreState = getModelState(preState, stateSet, true);
                        createLink(modelPreState, transitionTrigger);
                        createLink(transitionTrigger, anyState);
                    }
                } else {
                    final boolean simpleTransition;
                    if (Preferences.getHideExpectedTransitionLinks() && (preStates.size() == 1) && (postStates.size() == 1)) {
                        com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelPreState = getModelState(preStates.iterator().next(), stateSet, true);
                        com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelPostState = getModelState(postStates.iterator().next(), stateSet, false);
                        final boolean follows;
                        if (modelPreState instanceof Start && modelPostState instanceof End) {
                            follows = true;
                        } else if (modelPreState instanceof Start) {
                            follows = modelStates.indexOf(modelPostState) == 0;
                        } else if (modelPostState instanceof End) {
                            follows = modelStates.indexOf(modelPreState) == (modelStates.size() - 1);
                        } else {
                            follows = modelStates.indexOf(modelPreState) == (modelStates.indexOf(modelPostState) - 1);
                        }
                        simpleTransition = follows && !hasSingleWorkflowTransitionIncomings(modelPostState);
                    } else {
                        simpleTransition = false;
                    }
                    TransitionTrigger transitionTrigger = new TransitionTriggerImpl(transition.getUuid(), simpleTransition);
                    myX2Model.put(transition, transitionTrigger);
                    modelTriggers.add(transitionTrigger);
                    for (State preState : preStates) {
                        com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelPreState = getModelState(preState, stateSet, true);
                        createLink(modelPreState, transitionTrigger);
                    }
                    for (State postState : postStates) {
                        com.ivis.xprocess.ui.workflowdesigner.diagram.model.State modelPostState = getModelState(postState, stateSet, false);
                        createLink(transitionTrigger, modelPostState);
                    }
                }
            }
            modelStateSet.addChild(anyState);
            if (myStart != null) {
                anyState.addChild(myStart);
            }
            anyState.addChildren(modelStates);
            if (myEnd != null) {
                anyState.addChild(myEnd);
            }
            anyState.addChildren(modelTriggers);
            modelStateSet.addChildren(modelTriggersAnyState);
        }

        private static void createLink(Node source, Node target) {
            if (source instanceof TransitionTrigger || target instanceof TransitionTrigger) {
                Link link = source.getIncomingLink(target);
                if (link == null) {
                    link = target.getOutgoingLink(source);
                }
                if (link != null) {
                    link.setBidirectional(true);
                } else {
                    new LinkImpl(true, source, target);
                }
            } else {
                new LinkImpl(false, source, target);
            }
        }

        private static boolean hasSingleWorkflowTransitionIncomings(com.ivis.xprocess.ui.workflowdesigner.diagram.model.State state) {
            for (Link link : state.getIncomingLinks()) {
                if (Element.Type.WorkflowTransition.equals(link.getType())) {
                    TransitionTrigger trigger = (TransitionTrigger) link.getSource();
                    if (trigger.isSimple()) {
                        return true;
                    }
                }
            }
            return false;
        }

        public Node getRoot() {
            return myRootNode;
        }

        public Map<Xelement, Element> getMap() {
            return myX2Model;
        }

        private com.ivis.xprocess.ui.workflowdesigner.diagram.model.State getModelState(State state, StateSet stateSet, boolean preNotPostState) {
            if (preNotPostState) {
                if (state.equals(stateSet.getTheNonExistentState())) {
                    return myStart;
                } else {
                    return (com.ivis.xprocess.ui.workflowdesigner.diagram.model.State) myX2Model.get(state);
                }
            } else {
                if (state.equals(stateSet.getTheNonExistentState())) {
                    return myEnd;
                } else {
                    return (com.ivis.xprocess.ui.workflowdesigner.diagram.model.State) myX2Model.get(state);
                }
            }
        }
    }
}
