package com.marco.automate;

import java.util.List;

public interface AutomateModel {

    public void setEnterState(State st);

    public void setAsExitState(State st);

    public State getEnterState();

    public void addState(State st);

    public void addTransition(Transition tr, State from, State to);

    public List<Transition> getTransitions(State st);

    public State getFromState(Transition t);

    public State getToState(Transition t);
}
