package com.bbn.vessel.core.runtime.data;

/**
 * This class provides runtime functionality for a graph node.
 * <p>
 * Adds data values together.
 * <p>
 * This adder expects "a" and "b" input values.
 * <p>
 * Another design option is to have a single input terminal and add all the
 * connected values, similar to how an "IsAnd" has a single input.
 */
public final class EvalSubtract extends EvalBase {

    @Override
    protected DataValue compute(DataValue va, DataValue vb) {
        return (va == null ? vb : va.subtract(vb));
    }
}
