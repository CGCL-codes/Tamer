package com.ibm.wala.fixpoint;

import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.MonitorUtil.IProgressMonitor;

/**
 * Solves a set of constraints
 */
@SuppressWarnings("rawtypes")
public interface IFixedPointSolver<T extends IVariable> {

    /**
   * @return the set of statements solved by this {@link IFixedPointSolver}
   */
    public IFixedPointSystem<T> getFixedPointSystem();

    /**
   * Solve the problem.
   * <p>
   * PRECONDITION: graph is set up
   * 
   * @return true iff the evaluation of some constraint caused a change in the
   *         value of some variable.
   */
    public boolean solve(IProgressMonitor monitor) throws CancelException;
}
