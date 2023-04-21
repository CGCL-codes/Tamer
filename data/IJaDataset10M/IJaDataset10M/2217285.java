package iclab.optimization.heuristics;

import iclab.exceptions.ICParameterException;

/**
 * Generic interfaze for optimization algorithm. This interface contains the common functions
 * used by any optimization algorithm
 */
public interface ICProblem {

    /**
   * Function to evaluate a solution
   * @param solution Solution to evaluate
   * @return Evaluation of the solution
   * @throws ICParameterException
   */
    public double evaluate(Object solution) throws ICParameterException;
}
