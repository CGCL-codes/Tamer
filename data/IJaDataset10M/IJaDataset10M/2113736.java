package de.tum.in.botl.math.equationSolver.impl;

import java.util.StringTokenizer;
import symbolic.Math_interpreter;
import de.tum.in.botl.math.MultipleSolutionsException;
import de.tum.in.botl.math.equationSolver.EquationSolver;
import de.tum.in.botl.transformer.TransformationException;
import de.tum.in.botl.util.stringHelper.StringHelper;

/**
 * @author marschal
 * 10.09.2003
 * the equation solver seems to have a problem with 
 * variables that contain numbers and have any letters after
 * them, like VAR123VAR :-(
 * @param term A term like a + 7 whereby the equation would be 0 = a + 7
 * @param variable The unknown variable in the equation (e.g. a)
 */
public class EquationSolverImpl implements EquationSolver {

    private StringHelper stringHelper;

    /**
   * @param stringHelper
   */
    public EquationSolverImpl(StringHelper stringHelper) {
        super();
        this.stringHelper = stringHelper;
    }

    /**
   * @see de.tum.in.botl.math.equationSolver.EquationSolver#solveEquation(java.lang.String, java.lang.String)
   */
    public String solveEquation(String term, String variable) throws TransformationException, MultipleSolutionsException {
        String result;
        String input = "SOLVE(" + term + " = 0, " + variable + ")";
        try {
            Math_interpreter interpreter = new Math_interpreter();
            result = interpreter.calculate(input);
        } catch (Exception e) {
            throw new TransformationException("Equation solver failed to solve equation \"" + input + "\" for the variable \"" + variable + "\": " + e.getClass().getName() + ": " + e.getMessage());
        }
        if (result.startsWith("Cannot solve:") || result.startsWith("not_implemented")) throw new TransformationException("Equation solver failed to solve equation \"" + input + "\" for the variable \"" + variable + "\":\n" + result); else if ((new StringTokenizer(result, "=")).countTokens() > 2) {
            throw new MultipleSolutionsException("Equation solver failed to solve equation \"" + input + "\" for the variable \"" + variable + "\":\n" + result);
        }
        return stringHelper.split(result, "=")[1];
    }
}
