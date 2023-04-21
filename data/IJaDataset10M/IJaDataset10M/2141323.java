package de.denkselbst.niffler.search.niffler;

import java.util.BitSet;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import de.denkselbst.niffler.bottomclause.BottomClause;
import de.denkselbst.niffler.refinement.State;
import de.denkselbst.prologinterface.terms.PrologVariable;

/**
 * Calculates minimum number of literals that have to be added to a candidate
 * clause so as to achieve io-completeness (i.e. all head literal output
 * variables are bound). 
 * 
 * @author patrick
 */
public class IoCompletenessCalculator {

    /**
     * A constant indicating that h is infinite.
     */
    public static final int INCOMPLETABLE = -1;

    private BottomClause bc;

    public IoCompletenessCalculator(BottomClause bc) {
        this.bc = bc;
    }

    public int getMinimumNumberOfLiteralsRequiredForIoCompleteness(State state) {
        return this.getMinimumNumberOfLiteralsThatMustBeAdded(state.getAvailableInputVariables());
    }

    /**
     * State for best-first search to compute h for a given subset
     * of the bottom clause. h is the minimal number of literals that have
     * to be added in order to instantiate the head output variables.
     * 
     * @author patrick
     */
    private static class OutlookState implements Comparable {

        /**
         * Variables available as inputs to further literals.
         */
        private Set<PrologVariable> available;

        /**
         * Literals added so far.
         */
        private BitSet addedLiterals;

        /**
         * Constructor.
         * 
         * @param consumable the variables available for consumption.
         */
        public OutlookState(Set<PrologVariable> consumable, BitSet literalSelection) {
            this.available = new HashSet<PrologVariable>(consumable);
            this.addedLiterals = literalSelection;
        }

        /**
         * Implementation of Comparable interface.
         * 
         * @param o State to compare to.
         * 
         * @return -1, 0, or 1 if this state is smaller than, equals to or
         * greater than <code>0</code>.
         */
        public int compareTo(Object o) {
            return this.addedLiterals.cardinality() - ((OutlookState) o).addedLiterals.cardinality();
        }

        @Override
        public int hashCode() {
            return this.addedLiterals.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof OutlookState) {
                OutlookState other = (OutlookState) o;
                return this.addedLiterals.equals(other.addedLiterals);
            }
            return false;
        }
    }

    /**
     * Calculates lookahead h -- the minimum number of literals that have to
     * be added in order to instantiate all head output variables.
     *  
     * h is 0 if there are no head output variables or the head output
     * variables are already defined by the given subset.
     * 
     * If it is impossible to complete the subset given the parameter k,
     * the constant INCOMPLETABLE is returned.
     * 
     * @param available the input variables currently available. 
     *  
     * @return the minimum number of literals to add in order to instantiate
     * all head output variables.
     */
    protected int getMinimumNumberOfLiteralsThatMustBeAdded(Set<PrologVariable> available) {
        if (this.bc.getHeadConsumedVariables().isEmpty()) {
            return 0;
        }
        if (available.containsAll(this.bc.getHeadConsumedVariables())) {
            return 0;
        }
        PriorityQueue<OutlookState> agenda = new PriorityQueue<OutlookState>();
        OutlookState initial = new OutlookState(available, new BitSet());
        agenda.add(initial);
        Set<OutlookState> done = new HashSet<OutlookState>();
        while (!agenda.isEmpty()) {
            OutlookState s = agenda.remove();
            done.add(s);
            if (s.available.containsAll(this.bc.getHeadConsumedVariables())) {
                return s.addedLiterals.cardinality();
            }
            for (int i = 0; i < this.bc.bodySize(); i++) {
                if (s.addedLiterals.get(i)) {
                    continue;
                }
                Set<PrologVariable> required = this.bc.getConsumedVariables(i);
                if (!s.available.containsAll(required)) {
                    continue;
                }
                Set<PrologVariable> produced = this.bc.getProducedVariables(i);
                if (s.available.containsAll(produced)) {
                    continue;
                }
                BitSet larger = new BitSet();
                larger.or(s.addedLiterals);
                larger.set(i);
                Set<PrologVariable> avail2 = new HashSet<PrologVariable>(s.available);
                avail2.addAll(produced);
                OutlookState successor = new OutlookState(avail2, larger);
                if (!done.contains(successor)) {
                    agenda.add(successor);
                }
            }
        }
        return INCOMPLETABLE;
    }
}
