package cern.colt.matrix.linalg;

import cern.colt.matrix.DoubleMatrix2D;

/**
 * Interface that represents a function object: a function that takes 
 * two arguments and returns a single value.
 */
public interface Matrix2DMatrix2DFunction {

    /**
 * Applies a function to two arguments.
 *
 * @param x   the first argument passed to the function.
 * @param y   the second argument passed to the function.
 * @return the result of the function.
 */
    public abstract double apply(DoubleMatrix2D x, DoubleMatrix2D y);
}
