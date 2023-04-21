package ingenias.testing;

import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class State {

    private Vector<EventHandler> transitions = new Vector<EventHandler>();

    private String name;

    public State(String name) {
        this.name = name;
    }

    public synchronized Vector<State> evaluate(Event event) {
        Vector<State> result = new Vector<State>();
        for (EventHandler handler : transitions) {
            if (handler.evaluate(event, this)) {
                for (State st : handler.getNextStates()) result.add(st);
            }
        }
        return result;
    }

    public synchronized void addTransition(EventHandler eventHandler) {
        this.transitions.add(eventHandler);
    }

    public String toString() {
        return name;
    }
}
