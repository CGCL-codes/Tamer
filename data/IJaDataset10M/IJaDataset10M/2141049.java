package com.ibm.wala.dataflow.graph;

import com.ibm.wala.fixpoint.BooleanVariable;

/**
 * A {@link DataflowSolver} specialized for {@link BooleanVariable}s
 */
public class BooleanSolver<T> extends DataflowSolver<T, BooleanVariable> {

    public BooleanSolver(IKilldallFramework<T, BooleanVariable> problem) {
        super(problem);
    }

    @Override
    protected BooleanVariable makeNodeVariable(T n, boolean IN) {
        return new BooleanVariable();
    }

    @Override
    protected BooleanVariable makeEdgeVariable(T src, T dst) {
        return new BooleanVariable();
    }

    @Override
    protected BooleanVariable[] makeStmtRHS(int size) {
        return new BooleanVariable[size];
    }
}
