package solver.inputset;

import solver.constraint.Statistics;

/**
 * Interface for selecting tuples or parts of tuples from a given set of tuples
 * and checking the constraints which are defined on them. The selection only
 * depends on the position of the tuples in the input set not their contents.
 * 
 * @author thammers
 * 
 */
public interface Subset {

    /**
     * Checks all constraints which belong to the selected tuples and returns
     * and integer value representing the penalty for unsatisfied constraints.
     * 
     * @param candidate
     *                on array of tuples
     * @return penalty for unsatisfied constraints
     */
    public int checkConstraints(final Tuple[] candidate, final Statistics stats);

    /**
     * Selects tuples from a given set of tuples. All tuples which are not
     * selected by this component should be set to null explicitly.
     * 
     * @param orig
     *                on array of tuples
     * @return the selected tuples
     */
    public Tuple[] select(final Tuple[] orig);

    /**
     * Returns a string description of the subset.
     * 
     * @return string description of the subset
     */
    public String getDescription();
}
