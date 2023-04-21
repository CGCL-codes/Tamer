package net.sourceforge.fsa2sat.fsa;

/**
 *
 * @author voronov
 */
public interface AutomatonM extends Automaton {

    public void addState(State state);

    public void addState(State state, boolean isInitial);

    public void addEvent(Event event);

    public void addArc(State fromState, State toState, Event event);
}
