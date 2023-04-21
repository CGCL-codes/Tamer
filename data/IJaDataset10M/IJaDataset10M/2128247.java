package com.ibm.wala.fixedpoint.impl;

import com.ibm.wala.fixpoint.IFixedPointSystem;
import com.ibm.wala.fixpoint.IVariable;

/**
 * Default implementation of a fixed point solver.
 */
public abstract class DefaultFixedPointSolver<T extends IVariable<?>> extends AbstractFixedPointSolver<T> {

    private final DefaultFixedPointSystem<T> graph;

    /**
   * @param expectedOut number of expected out edges in the "usual" case
   * for constraints .. used to tune graph representation
   */
    public DefaultFixedPointSolver(int expectedOut) {
        super();
        graph = new DefaultFixedPointSystem<T>(expectedOut);
    }

    public DefaultFixedPointSolver() {
        super();
        graph = new DefaultFixedPointSystem<T>();
    }

    public IFixedPointSystem<T> getFixedPointSystem() {
        return graph;
    }
}
