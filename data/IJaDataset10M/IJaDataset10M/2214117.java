package com.ibm.wala.dataflow.IFDS;

/**
 * Flow functions for a {@link PartiallyBalancedTabulationProblem}
 */
public interface IPartiallyBalancedFlowFunctions<T> extends IFlowFunctionMap<T> {

    /**
   * This version should work when the "call" instruction was never reached normally. This applies only when using partially
   * balanced parentheses.
   * 
   * @param src
   * @param dest
   * @return the flow function for a "return" edge in the supergraph from src->dest
   */
    public IFlowFunction getUnbalancedReturnFlowFunction(T src, T dest);
}
