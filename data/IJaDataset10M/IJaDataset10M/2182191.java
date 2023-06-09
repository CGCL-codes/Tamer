package jdd.des.automata.bdd;

import jdd.bdd.*;
import jdd.util.*;
import jdd.util.math.*;
import jdd.graph.*;
import jdd.des.automata.*;
import java.util.*;

/**
 * This class is a BDD representation of an automaton. It <b>must not</b> be created
 * directly by the user. Create a BDDAutomata from an Automata object instead!!.
 * (Note that there are some restriction when usong BDDAutoata too!)<br>
 *<br>
 * BDDAutomaton hold the internal BDD data for an automaton (transition relation etc.)<br>
 *<br>
 * @see BDDAutomata
 */
public class BDDAutomaton {

    private Automaton original;

    int[] bdd_var_s, bdd_var_sp;

    /** the bdd vars used in the state vvectors Q and Q'*/
    private int bdd_cube_s, bdd_cube_sp;

    /** cube for our [local] Q and Q' */
    private int bdd_i, bdd_m, bdd_f;

    /** initial/marked/forbidden states */
    private int bdd_delta;

    /** the transition relation T */
    private int bdd_delta_top;

    /** T^ ,  T after event compensation */
    private int bdd_care_event;

    /** events used in this automaton */
    private int bdd_care_state;

    /** states used in the state vector */
    private int bdd_keep;

    /** keep for this automaton */
    private int bits, states;

    /**
	 * initialize the automaton. must be called from an BDDAutomaton object.
	 * cannot be used until buildRelations() is called!
	 * @throws java.lang.IllegalArgumentException
	 */
    public BDDAutomaton(Automaton automaton, BDDAutomata bdd) {
        this.original = automaton;
        original.lockAutomaton();
        states = original.numOfNodes();
        bits = Digits.log2_ceil(states);
        int tmp;
        bdd_var_s = new int[bits];
        bdd_var_sp = new int[bits];
        bdd_cube_s = bdd_cube_sp = bdd_keep = 1;
        for (int i = 0; i < bits; i++) {
            bdd_var_s[i] = bdd.createVar();
            bdd_var_sp[i] = bdd.createVar();
            bdd_cube_s = bdd.andTo(bdd_cube_s, bdd_var_s[i]);
            bdd_cube_sp = bdd.andTo(bdd_cube_sp, bdd_var_sp[i]);
            tmp = bdd.ref(bdd.biimp(bdd_var_s[i], bdd_var_sp[i]));
            bdd_keep = bdd.andTo(bdd_keep, tmp);
            bdd.deref(tmp);
        }
        int i = 0;
        bdd_care_state = 0;
        for (Enumeration e = automaton.getNodes().elements(); e.hasMoreElements(); i++) {
            State s = (State) e.nextElement();
            int bdd_state = BDDUtil.numberToBDD(bdd, bdd_var_s, i);
            int bdd_state_p = BDDUtil.numberToBDD(bdd, bdd_var_sp, i);
            s.setBDDState(bdd_state);
            s.setBDDStateP(bdd_state_p);
            bdd_care_state = bdd.orTo(bdd_care_state, bdd_state);
        }
    }

    public void cleanup() {
        original.unlockAutomaton();
    }

    void buildRelations(BDDAutomata bdd) {
        bdd_i = bdd_f = bdd_m = 0;
        for (Enumeration e = original.getNodes().elements(); e.hasMoreElements(); ) {
            State s = (State) e.nextElement();
            int bdd_state = s.getBDDState();
            if (s.isInitial()) bdd_i = bdd.orTo(bdd_i, bdd_state);
            if (s.isMarked()) bdd_m = bdd.orTo(bdd_f, bdd_state);
            if (s.isForbidden()) bdd_f = bdd.orTo(bdd_m, bdd_state);
        }
        bdd_care_event = 0;
        for (Event event = original.getAlphabet().head(); event != null; event = event.next) bdd_care_event = bdd.orTo(bdd_care_event, event.parent.getBDD());
        bdd_delta = 0;
        for (Enumeration e = original.getEdges().elements(); e.hasMoreElements(); ) {
            Transition edge = (Transition) e.nextElement();
            State s1 = (State) edge.n1;
            State s2 = (State) edge.n2;
            int tmp = bdd.ref(bdd.and(s1.getBDDState(), s2.getBDDStateP()));
            tmp = bdd.andTo(tmp, edge.event.parent.getBDD());
            bdd_delta = bdd.orTo(bdd_delta, tmp);
            bdd.deref(tmp);
        }
        int not_care_events = bdd.ref(bdd.not(bdd_care_event));
        int tmp1 = bdd.ref(bdd.and(not_care_events, bdd_keep));
        bdd_delta_top = bdd.ref(bdd.or(tmp1, bdd_delta));
        bdd.deref(not_care_events);
        bdd.deref(tmp1);
    }

    public Automaton getAutomaton() {
        return original;
    }

    public int getNumBits() {
        return bits;
    }

    public int getNumStates() {
        return states;
    }

    public int getBDDCubeS() {
        return bdd_cube_s;
    }

    public int getBDDCubeSp() {
        return bdd_cube_sp;
    }

    public int getBDDI() {
        return bdd_i;
    }

    public int getBDDM() {
        return bdd_m;
    }

    public int getBDDF() {
        return bdd_f;
    }

    public int getBDDDelta() {
        return bdd_delta;
    }

    public int getBDDDeltaTop() {
        return bdd_delta_top;
    }

    public int getBDDCareEvent() {
        return bdd_care_event;
    }

    public int getBDDCareState() {
        return bdd_care_state;
    }

    public int getBDDKeep() {
        return bdd_keep;
    }

    public String toString() {
        return original.getName();
    }
}
