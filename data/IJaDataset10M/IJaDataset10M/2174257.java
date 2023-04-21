package net.sourceforge.fsa2sat.fsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.fsa2sat.fsa.Automaton.Type;

/**
 *
 * @author voronov
 */
public class AutomatonMArrayList extends ArrayList<State> implements AutomatonM {

    /**
   * Determines if a de-serialized file is compatible with this class.
   *
   * Maintainers must change this value if and only if the new version
   * of this class is not compatible with old versions. See Sun docs
   * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
   * /serialization/spec/version.doc.html> details. </a>
   *
   * Not necessary to include in first version of the class, but
   * included here as a reminder of its importance.
   */
    private static final long serialVersionUID = 7526471155622776147L;

    String name;

    Automaton.Type type = Type.SPECIFICATION;

    Alphabet alphabet = new AlphabetLinkedHashSet();

    State initialState = null;

    Map<State, Map<Event, State>> arcs = new HashMap<State, Map<Event, State>>();

    public AutomatonMArrayList(String name) {
        this.name = name;
    }

    public AutomatonMArrayList(String name, Automaton.Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean hasInitialState() {
        return initialState != null;
    }

    @Override
    public State getInitialState() {
        if (hasInitialState()) return initialState; else throw new IllegalStateException("initial state for automaton " + getName() + " was not specified yet");
    }

    @Override
    public int getNumberOfStates() {
        return this.size();
    }

    @Override
    public boolean eventIsEnabled(State fromState, Event event) {
        if (!arcs.containsKey(fromState)) return false;
        return (arcs.get(fromState)).containsKey(event);
    }

    @Override
    public State nextState(State fromState, Event event) {
        return (arcs.get(fromState)).get(event);
    }

    @Override
    public void addState(State state) {
        this.add(state);
    }

    @Override
    public void addEvent(Event event) {
        alphabet.add(event);
    }

    @Override
    public void addArc(State fromState, State toState, Event event) {
        if (!getAlphabet().contains(event)) throw new IllegalArgumentException("error adding arc: " + "event " + event.getLabel() + " do not belong to the automaton " + getName());
        if (!this.contains(fromState)) throw new IllegalArgumentException("error adding arc: " + "source state " + fromState.getName() + " do not belong to the automaton " + getName());
        if (!this.contains(toState)) throw new IllegalArgumentException("error adding arc: " + "destination state " + toState.getName() + " do not belong to the automaton " + getName());
        if (!arcs.containsKey(fromState)) arcs.put(fromState, new HashMap<Event, State>());
        arcs.get(fromState).put(event, toState);
    }

    @Override
    public void addState(State state, boolean isInitial) {
        addState(state);
        if (isInitial) initialState = state;
    }
}
