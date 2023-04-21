package net.sourceforge.pseudoq.solver;

/**
 * A deductive strategy for values in the grid.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public interface Strategy {

    /**
     * Evaluates the current grid to try and deduce another value.
     * @return A solved cell, or null if no value can be deduced using this
     * strategy.
     * @throws ObsoleteStrategyException If this strategy is now useless and
     * should not be tried again, as values have been found for all the cells
     * it considers.
     * @throws IllegalStateException If the grid or extra information used by
     * the strategy are in an inconsistent state, probably due to some coding
     * error.
     */
    public SolutionStep evaluate() throws ObsoleteStrategyException, UnsolveablePuzzleException;
}
