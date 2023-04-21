package mipt.math.sys.alt.test.fuzzy;

import mipt.math.Number;
import mipt.math.fuzzy.ExactNumber;
import mipt.math.sys.alt.DecisionProblem;
import mipt.math.sys.alt.DecisionSolution;
import mipt.math.sys.alt.impl.ArithmeticFunctionAlgorithm;
import mipt.math.sys.alt.solve.DecisionSolver;
import mipt.math.sys.alt.solve.ScoresDecisionSolver;
import mipt.math.sys.alt.solve.SimpleGroupingAlgorithm;
import mipt.math.sys.num.Algorithm;

/**
 * Arithmetic utility function (without conversion to preferences)
 * @see ResultsTest2008
 * @author Stadnichenko
 */
public class ResultTest2008ArithFuncUt extends ResultsTest2008 {

    static {
        clazz = ResultTest2008ArithFuncUt.class;
    }

    public ResultTest2008ArithFuncUt(String f) {
        super(f);
    }

    protected DecisionSolution getSolutionIn() {
        DecisionProblem problem = getProblem();
        DecisionSolver solver = getSolver();
        solver.setAlgorithm(getScoreAlgorithm());
        solver.setAlgorithm(new SimpleGroupingAlgorithm());
        DecisionSolution desSol = solver.solve(problem);
        return desSol;
    }

    protected Algorithm getScoreAlgorithm() {
        return new ArithmeticFunctionAlgorithm() {

            public mipt.math.Number createConstant(double value) {
                return new ExactNumber(value);
            }

            public Number createNumber(double value) {
                return new ExactNumber(value);
            }
        };
    }

    protected DecisionSolver getSolver() {
        return new ScoresDecisionSolver();
    }
}
