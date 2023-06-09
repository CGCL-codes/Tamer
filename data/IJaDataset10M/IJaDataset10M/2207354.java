package com.ibm.wala.fixpoint;

import com.ibm.wala.util.graph.INodeWithNumber;

/**
 * The general form of a statement definition in an iterative solver is: x &gt;= term, where
 * term can be any complex expression whose free variables are among the
 * IVariables of the constraint system
 * this {@link IFixedPointStatement}is part of (x represents the left-hand side of the
 * constraint). The interpretation of term (the right-hand side of the
 * constraint) must be monotone.
 * <p>
 * The list of free variables in term is obtained by invoking {@link #getRHS()},
 * and the left hand side variable is obtained by calling {@link #getLHS()}.
 * </p>
 * 
 * Intuitively, a statement definition corresponds to an "equation" in dataflow parlance, or
 * a "constraint" in constraint solvers.
 */
@SuppressWarnings("rawtypes")
public interface IFixedPointStatement<T extends IVariable> extends INodeWithNumber {

    /**
   * @return the left-hand side of this statement.
   */
    public T getLHS();

    /**
   * returns the list of free variables appearing in the right-hand side of the
   * statement
   */
    public T[] getRHS();

    /**
   * Evaluate this statement, setting a new value for the left-hand side. The
   * return value is one of the following:
   * <ul>
   * {@link FixedPointConstants#CHANGED},
   * {@link FixedPointConstants#CHANGED_AND_FIXED},
   * {@link FixedPointConstants#NOT_CHANGED}or
   * {@link FixedPointConstants#NOT_CHANGED_AND_FIXED}.
   * </ul>
   */
    abstract byte evaluate();

    /**
   * Does this statement definition contain an appearance of a given variable?
   * 
   * @param v the variable in question
   */
    public boolean hasVariable(T v);
}
