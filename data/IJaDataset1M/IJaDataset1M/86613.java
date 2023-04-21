package org.apache.commons.math.optimization;

import java.io.Serializable;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;

/** 
 * This interface represents an optimization algorithm for {@link MultivariateRealFunction
 * scalar objective functions}.
 * <p>Optimization algorithms find the input point set that either {@link GoalType
 * maximize or minimize} an objective function.</p>
 * @see DifferentiableMultivariateRealOptimizer
 * @see DifferentiableMultivariateVectorialOptimizer
 * @version $Revision: 758049 $ $Date: 2009-03-24 23:09:34 +0100 (Di, 24 Mrz 2009) $
 * @since 2.0
 */
public interface MultivariateRealOptimizer extends Serializable {

    /** Set the maximal number of iterations of the algorithm.
     * @param maxIterations maximal number of function calls
     */
    void setMaxIterations(int maxIterations);

    /** Get the maximal number of iterations of the algorithm.
     * @return maximal number of iterations
     */
    int getMaxIterations();

    /** Get the number of iterations realized by the algorithm.
     * <p>
     * The number of evaluations corresponds to the last call to the
     * {@link #optimize(MultivariateRealFunction, GoalType, double[]) optimize}
     * method. It is 0 if the method has not been called yet.
     * </p>
     * @return number of iterations
     */
    int getIterations();

    /** Get the number of evaluations of the objective function.
     * <p>
     * The number of evaluations corresponds to the last call to the
     * {@link #optimize(MultivariateRealFunction, GoalType, double[]) optimize}
     * method. It is 0 if the method has not been called yet.
     * </p>
     * @return number of evaluations of the objective function
     */
    int getEvaluations();

    /** Set the convergence checker.
     * @param checker object to use to check for convergence
     */
    void setConvergenceChecker(RealConvergenceChecker checker);

    /** Get the convergence checker.
     * @return object used to check for convergence
     */
    RealConvergenceChecker getConvergenceChecker();

    /** Optimizes an objective function.
     * @param f objective function
     * @param goalType type of optimization goal: either {@link GoalType#MAXIMIZE}
     * or {@link GoalType#MINIMIZE}
     * @param startPoint the start point for optimization
     * @return the point/value pair giving the optimal value for objective function
     * @exception FunctionEvaluationException if the objective function throws one during
     * the search
     * @exception OptimizationException if the algorithm failed to converge
     * @exception IllegalArgumentException if the start point dimension is wrong
     */
    RealPointValuePair optimize(MultivariateRealFunction f, GoalType goalType, double[] startPoint) throws FunctionEvaluationException, OptimizationException, IllegalArgumentException;
}
