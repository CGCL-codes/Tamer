package spinja.promela.compiler.automaton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import spinja.promela.compiler.actions.Action;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A {@link State} within a {@link Automaton} is simply a state that contains the following
 * information about this state.
 * 
 * <ul>
 * <li>The state id (useful for coding where you are)</li>
 * <li>If this state is in an atomic sequence</li>
 * <li>A list of output transitions</li>
 * <li>A list of input transitions</li>
 * <li>A list of labels that is attached to this state</li>
 * </ul>
 * 
 * @author Marc de Jonge
 */
public class State {

    private static class StateIdCounter {

        private static int id = 0;

        private static synchronized int nextId() {
            return id++;
        }
    }

    private boolean inAtomic;

    private int stateId;

    private final Automaton automaton;

    private final List<Transition> out, in;

    private final List<String> labels;

    /**
	 * Returns an {@link Iterable} that makes it possible to use the enhanced for-loop over all
	 * input transitions.
	 */
    public final Iterable<Transition> input = new Iterable<Transition>() {

        public Iterator<Transition> iterator() {
            return new Iterator<Transition>() {

                private int index = 0;

                private int size = in.size();

                public boolean hasNext() {
                    return index < size;
                }

                public Transition next() {
                    return in.get(index++);
                }

                public void remove() {
                    throw new NotImplementedException();
                }
            };
        }

        ;
    };

    /**
	 * Returns an {@link Iterable} that makes it possible to use the enhanced for-loop over all
	 * output transitions.
	 */
    public final Iterable<Transition> output = new Iterable<Transition>() {

        public Iterator<Transition> iterator() {
            return new Iterator<Transition>() {

                private int index = 0;

                private int size = out.size();

                public boolean hasNext() {
                    return index < size;
                }

                public Transition next() {
                    return out.get(index++);
                }

                public void remove() {
                    throw new NotImplementedException();
                }
            };
        }

        ;
    };

    /**
	 * Constructor of State. Should only be created from the graph.
	 * 
	 * @param automaton
	 *            The {@link Automaton} to which this State should belong.
	 * @param inAtomic
	 *            Indicates whether this state is in an atomic sequence.
	 */
    public State(final Automaton automaton, final boolean inAtomic) {
        this.automaton = automaton;
        this.inAtomic = inAtomic;
        this.stateId = StateIdCounter.nextId();
        out = new ArrayList<Transition>();
        in = new ArrayList<Transition>();
        labels = new ArrayList<String>();
    }

    /**
	 * Adds a certain label to this state.
	 * @param label
	 *            The name of the label
	 */
    public void addLabel(final String label) {
        labels.add(label);
    }

    void addOut(Transition trans) {
        out.add(trans);
    }

    void addOut(Transition trans, Transition after) {
        int index = out.indexOf(after);
        out.add(index + 1, trans);
    }

    void addIn(Transition trans) {
        in.add(trans);
    }

    void removeOut(Transition trans) {
        out.remove(trans);
    }

    void removeIn(Transition trans) {
        in.remove(trans);
        if (in.isEmpty()) {
            delete();
        }
    }

    /**
	 * Removes this state from the graph completely, including all attached transitions.
	 */
    public void delete() {
        while (!in.isEmpty()) {
            in.get(0).delete();
        }
        while (!out.isEmpty()) {
            out.get(0).delete();
        }
    }

    /**
	 * Returns true when this state is an ending state. A {@link State} is an ending state when
	 * either it has one or more {@link EndTransition} or {@link NeverEndTransition} going out of
	 * this state, or this state has a label beginning with "end".
	 * @return true when this state is an ending state.
	 */
    public boolean isEndingState() {
        for (Transition trans : out) {
            if (trans instanceof EndTransition || trans instanceof NeverEndTransition) return true;
        }
        return hasLabelPrefix("end");
    }

    /**
	 * Returns true when this state is and acceptance state. A {@link State} is an acceptance state
	 * when it has a label that starts with "accept".
	 * @return true when this state is and acceptance state.
	 */
    public boolean isAcceptState() {
        return hasLabelPrefix("accept");
    }

    /**
	 * Returns true when this state is and progress state. A {@link State} is an progress state when
	 * it has a label that starts with "progress".
	 * @return true when this state is and progress state.
	 */
    public boolean isProgressState() {
        return hasLabelPrefix("progress");
    }

    /**
	 * @param index
	 *            The index of the output transition.
	 * @return The output transition on the given index.
	 */
    public Transition getOut(int index) {
        return out.get(index);
    }

    /**
	 * @param index
	 *            The index of the input transition.
	 * @return The input transition on the given index.
	 */
    public Transition getIn(int index) {
        return in.get(index);
    }

    /**
	 * @return The unique id of this state.
	 */
    public int getStateId() {
        return stateId;
    }

    /**
	 * Sets the unique id of this state.
	 * @param stateId
	 *            The new unique id of the state.
	 */
    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    /**
	 * @param prefix
	 *            The prefix that we want to check for.
	 * @return True if one of the labels that is has begins with the given prefix, false otherwise.
	 */
    public boolean hasLabelPrefix(final String prefix) {
        for (final String label : labels) {
            if (label.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @return True when this state is in an atomic sequence, false otherwise.
	 */
    public boolean isInAtomic() {
        return inAtomic;
    }

    /**
	 * Sets the inAtomic
	 * @param inAtomic
	 */
    public void setInAtomic(boolean inAtomic) {
        this.inAtomic = inAtomic;
    }

    /**
	 * @return True when from this state, only local transitions can be taken.
	 */
    public boolean isLocal() {
        for (final Transition t : out) {
            if (!t.isLocal()) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Merges the given state with this one, copying all the
	 * @param other
	 */
    public void merge(final State other) {
        for (final Transition t : other.in) {
            t.setTo(this);
            in.add(t);
        }
        other.in.clear();
        for (final Transition t : other.out) {
            t.setFrom(this);
            out.add(t);
        }
        other.out.clear();
        if (automaton.getStartState() == other) {
            automaton.setStartState(this);
        }
    }

    /**
	 * @return The {@link Automaton} to which this {@link State} belongs.
	 */
    public Automaton getAutomaton() {
        return automaton;
    }

    /**
	 * @return The number of transitions that go to this state.
	 */
    public int sizeIn() {
        return in.size();
    }

    /**
	 * @return The number of transitions that go out from this state.
	 */
    public int sizeOut() {
        return out.size();
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return (inAtomic ? "Atomic " : "") + "State " + stateId + " (OUT: " + out + ")";
    }

    /**
	 * Creates a new {@link ActionTransition} to the given ending state.
	 * 
	 * @param end
	 *            The state to where the new {@link Transition} should go to.
	 * @return The new {@link Transition}.
	 */
    public Transition newTransition(State end) {
        Transition t = new ActionTransition(this, end);
        return t;
    }

    /**
	 * Creates a new {@link ActionTransition} from the given action and ending state.
	 * 
	 * @param action
	 *            The action the is to be executed by the newly created {@link Transition}.
	 * @param end
	 *            The state to where the new {@link Transition} should go to.
	 * @return The new {@link Transition}.
	 */
    public Transition newTransition(Action action, State end) {
        Transition t = new ActionTransition(this, end);
        t.addAction(action);
        return t;
    }
}
