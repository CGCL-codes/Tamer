package jmetal.problems.singleObjective;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

/**
 * Class representing problem Griewank
 */
public class Griewank extends Problem {

    /** 
   * Constructor
   * Creates a default instance of the Griewank problem
   * @param numberOfVariables Number of variables of the problem 
   * @param solutionType The solution type must "Real" or "BinaryReal". 
   */
    public Griewank(String solutionType, Integer numberOfVariables) throws ClassNotFoundException {
        numberOfVariables_ = numberOfVariables;
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 0;
        problemName_ = "Sphere";
        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];
        for (int var = 0; var < numberOfVariables_; var++) {
            lowerLimit_[var] = -600.0;
            upperLimit_[var] = 600.0;
        }
        if (solutionType.compareTo("BinaryReal") == 0) solutionType_ = new BinaryRealSolutionType(this); else if (solutionType.compareTo("Real") == 0) solutionType_ = new RealSolutionType(this); else {
            System.out.println("Error: solution type " + solutionType + " invalid");
            System.exit(-1);
        }
    }

    /** 
  * Evaluates a solution 
  * @param solution The solution to evaluate
   * @throws JMException 
  */
    public void evaluate(Solution solution) throws JMException {
        Variable[] decisionVariables = solution.getDecisionVariables();
        double sum = 0.0;
        double mult = 1.0;
        double d = 4000.0;
        for (int var = 0; var < numberOfVariables_; var++) {
            sum += decisionVariables[var].getValue() * decisionVariables[var].getValue();
            mult *= Math.cos(decisionVariables[var].getValue() / Math.sqrt(var + 1));
        }
        solution.setObjective(0, 1.0 / d * sum - mult + 1.0);
    }
}
