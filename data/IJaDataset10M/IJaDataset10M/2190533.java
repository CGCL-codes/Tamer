package automata.fsa;

import automata.Automaton;
import automata.State;
import automata.Transition;

/**
 * This subclass of <CODE>Automaton</CODE> is specifically for a
 * definition of a regular Finite State Automaton.
 */
public class FiniteStateAutomaton extends Automaton {

    private String ID;

    public String toString() {
        return ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public FiniteStateAutomaton() {
    }

    public FiniteStateAutomaton(String ID) {
        super();
        if (ID == null) this.ID = ""; else this.ID = ID;
    }

    /**
     * Returns the class of <CODE>Transition</CODE> this automaton
     * must accept.
     *
     * @return the <CODE>Class</CODE> object for
     *         <CODE>automata.fsa.FSATransition</CODE>
     */
    protected Class getTransitionClass() {
        return automata.fsa.FSATransition.class;
    }

    public Transition getTransitionByName(String s1, String label, String s2) {
        Transition[] transitions = getTransitions();
        State x1 = getStateWithName(s1);
        State x2 = getStateWithName(s2);
        for (Transition transition : transitions) {
            FSATransition fsaTrans = (FSATransition) transition;
            if ((fsaTrans.getLabel().equals(label)) && (fsaTrans.getFromState().equals(x1)) && (fsaTrans.getToState().equals(x2))) {
                return transition;
            }
        }
        return null;
    }

    public Object clone() {
        FiniteStateAutomaton clone = (FiniteStateAutomaton) super.clone();
        clone.setID(ID);
        return clone;
    }
}
