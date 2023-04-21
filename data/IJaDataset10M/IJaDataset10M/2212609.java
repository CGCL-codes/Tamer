package com.itmusings.stm.model;

import java.util.HashMap;
import java.util.Map;
import com.itmusings.stm.action.STMTransitionAction;

public class StateDescriptor implements TransientActionsAwareDescriptor {

    protected String id;

    protected boolean initialState;

    @SuppressWarnings("unchecked")
    protected STMTransitionAction entryAction;

    @SuppressWarnings("unchecked")
    protected STMTransitionAction exitAction;

    /**
	 * Is this state manual? (or a view state?)
	 */
    protected boolean manualState = false;

    public boolean isManualState() {
        return manualState;
    }

    public void setManualState(boolean manualState) {
        this.manualState = manualState;
    }

    private Map<String, Transition> transitions = new HashMap<String, Transition>();

    public StateDescriptor() {
        super();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }

    public boolean isInitialState() {
        return initialState;
    }

    public void setTransitions(Map<String, Transition> transitions) {
        this.transitions = transitions;
    }

    public Map<String, Transition> getTransitions() {
        return transitions;
    }

    public void addTransition(Transition transition) {
        transitions.put(transition.getEventId(), transition);
    }

    @SuppressWarnings("unchecked")
    public STMTransitionAction getEntryAction() {
        return entryAction;
    }

    @SuppressWarnings("unchecked")
    public void setEntryAction(STMTransitionAction entryAction) {
        this.entryAction = entryAction;
    }

    @SuppressWarnings("unchecked")
    public STMTransitionAction getExitAction() {
        return exitAction;
    }

    @SuppressWarnings("unchecked")
    public void setExitAction(STMTransitionAction exitAction) {
        this.exitAction = exitAction;
    }

    public void validate() throws Exception {
        if (id == null) throw new Exception("");
        for (Transition t : transitions.values()) {
            System.out.println(t);
        }
    }

    @Override
    public String toString() {
        return "StateDescriptor [id=" + id + ", initialState=" + initialState + ", transitions=" + transitions + "]";
    }
}
