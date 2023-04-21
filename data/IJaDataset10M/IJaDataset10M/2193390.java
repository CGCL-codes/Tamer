package mipt.math.function.array;

import mipt.math.function.MultiFunction;

/**
 * MutiVectorFunction that knows what its derivatives are.
 * @author Evdokimov
 */
public interface DifferentiableMultiVectorFunction extends MultiVectorFunction {

    /**
	 * @param functionIndex - index in this vector-function.
	 * @param argIndex - corresponds to the array index in calc(Number[] arg).
	 */
    MultiFunction getPartialDerivative(int functionIndex, int argIndex);

    /**
	 * @return matrix of derivatives: rows - functions, columns - arguments. 
	 */
    MultiMatrixFunction getJacobiMatrix();
}
