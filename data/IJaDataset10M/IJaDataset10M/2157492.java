package mipt.math.function.diff;

import mipt.math.Number;
import mipt.math.array.Matrix;
import mipt.math.array.Vector;
import mipt.math.function.array.MultiVectorFunction;
import mipt.math.function.array.VectorFunction;

/**
 * Differentiates VectorFunction numerically by 1 or by multiple variables 
 * TO DO: add optional argument for domain of function definition 
 */
public interface VectorDifferentiator {

    /**
	 * Returns dF/dx where x is scalar. F's elements can be both functions of one variable
	 * and MultiFunctions: F=F(y,x) where y is vector, and x is the last argument
	 * (if F or F[i] does not have x argument, result[i] must be zero!). 
	 * You can use values not to calculate func at x+getXIndices()[i]*step twice
	 * Values or values[i] can be null, but values[i]!=null can't contain null elements
	 * Values[i] will NOT be damaged during use  
	 * Values[i] must be set according to the differentiator method: see getXIndices() 
	 */
    Vector calcDerivative(VectorFunction func, Number x, Number step, Vector[] values);

    /**
	 * Returns Jacobi matrix ||dF/dy|| for the given MultiVectorFunction F(y).
	 * It can be assumed that step is common for all x's; then steps.length == 1 
	 * You can use values not to calculate func at x+getXIndices()[i]*step twice
	 * Values or values[i] can be null, but values[i]!=null can't contain null elements  
	 * Values[i] will NOT be damaged during use  
	 * Values[i] must be set according to the differentiator method: see getXIndices() 
	 */
    Matrix calcDerivatives(MultiVectorFunction func, Number[] xs, Number[] steps, Vector[] values);

    /**
	 * Return indices inds relative to x: func.calcVector(x+inds[i]*step)
	 *   can be send to calcDerivative() as values[i].
	 *   The indices are often (but not always) integer. 
	 */
    double[] getXIndices();

    /**
	 * Returns order of accuracy
	 * Is often useful to check if getIndices() contains 0 (true for odd orders)  
	 */
    int getOrder();
}
