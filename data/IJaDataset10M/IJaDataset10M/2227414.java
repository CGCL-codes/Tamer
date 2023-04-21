package org.jlib.games.sudoku.solver;

import org.jlib.core.reflection.ClassInstantiationException;

/**
 * Factory for AllSolutionsSolvers.
 * 
 * @param <Item>
 *        type of the items
 * @author Igor Akkerman
 */
public interface AllSolutionsSolverFactory<Item> {

    /**
     * Creates a new AllSolutionsSolver.
     * 
     * @return the newly created Solver
     * @throws ClassInstantiationException
     *         if the Solver cannot be instantiated
     */
    public abstract AllSolutionsSolver<Item> newSingleSolutionSolver() throws ClassInstantiationException;
}
