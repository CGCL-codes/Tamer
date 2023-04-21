package org.sepa.commons.automatons;

import java.util.Set;
import org.sepa.commons.grammar.Symbol;

/**
 *
 * @author plb
 */
public interface Automaton {

    /**
     * Get the complete set of transitions in the automaton
     * @return
     *          a not-null unmodifiable set
     */
    Set<Transition> getTransitions();

    /**
     * Get the set of transitions for a given state
     * @param state
     *          a not-null <code>State</code>
     * @return
     *          a not-null set
     */
    Set<Transition> getTransitionsFrom(final State from);

    /**
     * Get the set of transitions for a given state with a given symbol
     * @param state
     *          a not-null <code>State</code>
     * @param symbol
     *          a not-null <code>Symbol</code>
     * @return
     *          a not-null set
     */
    Set<Transition> getTransitions(final State from, final Symbol symbol);

    /**
     * Get all states in the automaton
     * @return
     *          a not-null set
     */
    Set<State> getStates();

    /**
     * Get the automaton's start state
     * @return
     *          a not-null state
     */
    State getStartState();

    /**
     * Get all accepting states in the automaton
     * @return
     *          a not-null set
     */
    Set<AcceptingState> getAcceptingStates();
}
