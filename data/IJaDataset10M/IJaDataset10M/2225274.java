package com.ibm.wala.dataflow.graph;

import com.ibm.wala.fixpoint.AbstractOperator;
import com.ibm.wala.fixpoint.IVariable;

/**
 * Abstract superclass for meet operators
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractMeetOperator<T extends IVariable> extends AbstractOperator<T> {

    /**
   * subclasses can override if needed
   * @return true iff this meet is a noop when applied to one argument
   */
    public boolean isUnaryNoOp() {
        return true;
    }
}
