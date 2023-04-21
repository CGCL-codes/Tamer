package net.matmas.pnapi;

import java.util.ArrayList;

/**
 * Represents a sequence of transitions.
 * @author matmas
 */
public class FiringSequence extends ArrayList<Transition> implements Comparable<FiringSequence> {

    public int compareTo(FiringSequence firingSequence) {
        return this.toString().compareTo(firingSequence.toString());
    }

    /**
	 * One transition can be more then one time in firing sequence.
	 * Returns number of occurences of the specified transition.
	 * @param transition transition to determine number of occurences
	 * @return number of occurences of the specified transition
	 */
    public int getNumOfTransition(Transition transition) {
        int num = 0;
        for (Transition t : this) {
            if (t == transition) {
                num++;
            }
        }
        return num;
    }

    /**
	 * Returns last transition in this firing sequence.
	 * @return last transition in the firing sequence
	 */
    public Transition getLastTransition() {
        return this.get(this.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Transition transition : this) {
            result.append(transition.getLabel() + " ");
        }
        return result.toString();
    }
}
