package org.softmed.action.states;

import java.util.ArrayList;
import java.util.List;
import org.softmed.action.ActionStep;
import org.softmed.action.workarea.WorkArea;

public abstract class State implements ActionStep {

    protected String name;

    protected List<ActionStep> actionSteps = new ArrayList<ActionStep>();

    protected WorkArea workArea;

    protected ActionStep failedAction;

    protected Throwable exception;

    protected State success;

    protected State failure;

    protected EventMap eventMap = new EventMap();

    protected StateChangeListener stateChangeListener;

    private Object readResolve() {
        actionSteps = new ArrayList<ActionStep>();
        return this;
    }

    public State(String name) {
        this.name = name;
    }

    @Override
    public abstract void execute() throws Throwable;

    @Override
    public void setWorkArea(WorkArea workArea) {
        if (this.workArea != null) workArea.clearListeners();
        this.workArea = workArea;
        for (ActionStep action : actionSteps) {
            action.setWorkArea(workArea);
        }
    }

    @Override
    public void dispose() throws Throwable {
        for (ActionStep action : actionSteps) {
            try {
                action.dispose();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        actionSteps.clear();
        actionSteps = null;
        failure = null;
        success = null;
        eventMap.dispose();
        eventMap = null;
        stateChangeListener = null;
    }

    public List<ActionStep> getActionSteps() {
        return actionSteps;
    }

    public String getName() {
        return name;
    }

    public WorkArea getWorkArea() {
        return workArea;
    }

    public void addStep(ActionStep action) {
        actionSteps.add(action);
    }

    public void addStep(int index, ActionStep action) {
        actionSteps.add(index, action);
    }

    public State getFailure() {
        return failure;
    }

    public void setFailure(State failure) {
        this.failure = failure;
    }

    public EventMap getEventMap() {
        return eventMap;
    }

    public State getSuccess() {
        return success;
    }

    public void setSuccess(State success) {
        this.success = success;
    }

    public StateChangeListener getStateChangeListener() {
        return stateChangeListener;
    }

    public void setStateChangeListener(StateChangeListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }

    protected void goSuccess() throws Throwable {
        if (success != null) {
            success.execute();
        }
    }

    protected void goFailure() throws Throwable {
        if (failure != null) {
            failure.execute();
        }
    }

    public Throwable getException() {
        return exception;
    }
}
