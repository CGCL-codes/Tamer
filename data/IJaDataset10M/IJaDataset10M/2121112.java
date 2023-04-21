package de.nameless.gameEngine.util.StateMachine;

import java.util.HashMap;
import java.util.Map;

public class State {

    public String name;

    public Map<Action, State> transiton;

    public State(String name) {
        this.name = name;
        transiton = new HashMap<Action, State>();
    }

    public void addTransition(Action action, State nextState) {
        transiton.put(action, nextState);
    }

    @Override
    public String toString() {
        return "State:" + name;
    }
}
