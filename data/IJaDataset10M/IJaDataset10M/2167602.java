package jmetal.base;

import java.io.Serializable;
import jmetal.base.Configuration.*;
import jmetal.base.variable.Binary;
import jmetal.base.variable.BinaryReal;
import jmetal.base.variable.Int;
import jmetal.base.variable.Permutation;
import jmetal.base.variable.Real;

/** 
 * This class contains the decision variables of a solution
 */
public class DecisionVariables implements Serializable {

    /**
   * Stores the decision variables of a solution
   */
    public Variable[] variables_;

    /**
   * The problem to solve
   */
    private Problem problem_;

    /**
   * Constructor
   * @param problem The problem to solve
   */
    public DecisionVariables(Problem problem) {
        problem_ = problem;
        variables_ = new Variable[problem_.getNumberOfVariables()];
        if (problem.solutionType_ == SolutionType_.Binary) {
            for (int var = 0; var < problem_.getNumberOfVariables(); var++) variables_[var] = new Binary(problem_.getLength(var));
        } else if (problem.solutionType_ == SolutionType_.Real) {
            for (int var = 0; var < problem_.getNumberOfVariables(); var++) variables_[var] = new Real(problem_.getLowerLimit(var), problem_.getUpperLimit(var));
        } else if (problem.solutionType_ == SolutionType_.Int) {
            for (int var = 0; var < problem_.getNumberOfVariables(); var++) variables_[var] = new Int((int) problem_.getLowerLimit(var), (int) problem_.getUpperLimit(var));
        } else if (problem.solutionType_ == SolutionType_.Permutation) {
            for (int var = 0; var < problem_.getNumberOfVariables(); var++) variables_[var] = new Permutation(problem_.getLength(var));
        } else if (problem.solutionType_ == SolutionType_.BinaryReal) {
            for (int var = 0; var < problem_.getNumberOfVariables(); var++) {
                if (problem.getPrecision() == null) {
                    int[] precision = new int[problem.getNumberOfVariables()];
                    for (int i = 0; i < problem.getNumberOfVariables(); i++) precision[i] = jmetal.base.Configuration.DEFAULT_PRECISION;
                    problem.setPrecision(precision);
                }
                variables_[var] = new BinaryReal(problem_.getPrecision(var), problem_.getLowerLimit(var), problem_.getUpperLimit(var));
            }
        } else if (problem.solutionType_ == SolutionType_.IntReal) {
            for (int var = 0; var < problem_.getNumberOfVariables(); var++) if (problem.variableType_[var] == VariableType_.Int) variables_[var] = new Int((int) problem_.getLowerLimit(var), (int) problem_.getUpperLimit(var)); else if (problem.variableType_[var] == VariableType_.Real) variables_[var] = new Real(problem_.getLowerLimit(var), problem_.getUpperLimit(var)); else {
                Configuration.logger_.severe("DecisionVariables.DecisionVariables: " + "error creating a Solution of type IntReal");
            }
        } else {
            Configuration.logger_.severe("DecisionVariables.DecisionVariables: " + "the solutio type " + problem.solutionType_ + " is incorrect");
        }
    }

    /**
   * Copy constructor
   * @param decisionVariables The <code>DecisionVariables<code> object to copy.
   */
    public DecisionVariables(DecisionVariables decisionVariables) {
        problem_ = decisionVariables.problem_;
        variables_ = new Variable[decisionVariables.variables_.length];
        for (int var = 0; var < decisionVariables.variables_.length; var++) {
            variables_[var] = decisionVariables.variables_[var].deepCopy();
        }
    }

    /**
   * Returns the number of decision variables.
   * @return The number of decision variables.
   */
    public int size() {
        return problem_.getNumberOfVariables();
    }

    /** Returns a String that represent the DecisionVariable
   * @return The string.
   */
    public String toString() {
        String aux = "";
        for (int i = 0; i < variables_.length; i++) {
            aux += " " + variables_[i].toString();
        }
        return aux;
    }
}
