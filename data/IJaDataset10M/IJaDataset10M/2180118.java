package jeco.lib.problems.dtlz;

import java.util.ArrayList;
import jeco.kernel.problem.Problem;
import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.operator.comparator.SolutionDominance;
import jeco.kernel.problem.Variable;

public class DTLZ5 extends DTLZ {

    public DTLZ5(Integer numberOfVariables) {
        super("DTLZ5", numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            lowerBound[i] = 0.0;
            upperBound[i] = 1.0;
        }
    }

    public DTLZ5() {
        this(12);
    }

    public void evaluate(Solution<Variable<Double>> solution) {
        double[] theta = new double[numberOfObjectives];
        int k = numberOfVariables - numberOfObjectives + 1;
        double g = 0;
        ArrayList<Variable<Double>> variables = solution.getVariables();
        for (int i = numberOfVariables - k + 1; i <= numberOfVariables; i++) {
            g += Math.pow(variables.get(i - 1).getValue() - 0.5, 2);
        }
        double t = Math.PI / (4.0 * (1.0 + g));
        theta[0] = variables.get(0).getValue() * Math.PI / 2.0;
        for (int i = 2; i <= (numberOfObjectives - 1); i++) {
            theta[i - 1] = t * (1.0 + 2.0 * g * variables.get(i - 1).getValue());
        }
        for (int i = 1; i <= numberOfObjectives; i++) {
            double f = (1 + g);
            for (int j = numberOfObjectives - i; j >= 1; j--) {
                f *= Math.cos(theta[j - 1]);
            }
            if (i > 1) {
                f *= Math.sin(theta[numberOfObjectives - i]);
            }
            solution.getObjectives().set(i - 1, f);
        }
    }

    public Solutions<Variable<Double>> computeParetoOptimalFront(int n) {
        int num = (int) Math.sqrt(n);
        Solutions<Variable<Double>> result = new Solutions<Variable<Double>>();
        double x0, x1;
        for (int i = 0; i < num; ++i) {
            x0 = 0.0 + (1.0 * i) / (num - 1);
            for (int j = 0; j < num; ++j) {
                Solution<Variable<Double>> sol = new Solution<Variable<Double>>(this);
                x1 = 0.0 + (1.0 * j) / (num - 1);
                sol.getVariables().set(0, new Variable<Double>(x0));
                sol.getVariables().set(1, new Variable<Double>(x1));
                for (int k = 2; k < numberOfVariables; ++k) {
                    sol.getVariables().set(k, new Variable<Double>(0.5));
                }
                evaluate(sol);
                result.add(sol);
            }
        }
        result.keepParetoNonDominated(new SolutionDominance<Variable<Double>>());
        return result;
    }

    @Override
    public Problem<Variable<Double>> clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
