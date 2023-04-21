package com.dhb.optimizing;

import com.dhb.interfaces.ManyVariableFunction;

public abstract class MultiVariableOptimizer extends com.dhb.iterations.IterativeProcess {

    /**
	 * Value of the function to optimize.
	 */
    protected ManyVariableFunction f;

    /**
	 * Best value found so far: must be set to determine the dimension of the argument of the function.
	 */
    protected double[] result;

    /**
	 * Optimizing strategy (minimum or maximum).
	 */
    protected OptimizingPointFactory pointFactory;

    /**
 * MultiVariableOptimizer constructor comment.
 */
    public MultiVariableOptimizer(ManyVariableFunction func, OptimizingPointFactory pointCreator, double[] initialValue) {
        f = func;
        pointFactory = pointCreator;
        setInitialValue(initialValue);
    }

    /**
 * @return double[]	result of the receiver
 */
    public double[] getResult() {
        return result;
    }

    /**
 * This method was created in VisualAge.
 * @param v double[]
 */
    public void setInitialValue(double[] v) {
        result = v;
    }

    /**
 * Use bubble sort to sort the best points
 */
    protected void sortPoints(OptimizingVector[] bestPoints) {
        OptimizingVector temp;
        int n = bestPoints.length;
        int bound = n - 1;
        int i, m;
        while (bound >= 0) {
            m = -1;
            for (i = 0; i < bound; i++) {
                if (bestPoints[i + 1].betterThan(bestPoints[i])) {
                    temp = bestPoints[i];
                    bestPoints[i] = bestPoints[i + 1];
                    bestPoints[i + 1] = temp;
                    m = i;
                }
            }
            bound = m;
        }
    }
}
