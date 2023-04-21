package game.trainers.gradient.numopt;

import common.MachineAccuracy;
import common.function.NumericalDifferentiation;
import common.function.ObjectiveFunction;

/**
 * User: drchaj1
 * Date: 31.3.2007
 * Time: 20:22:15
 */
public class SteepestDescentMinimization extends MinimizationMethod {

    private double maxIterations;

    private final ObjectiveFunction func;

    private final int n;

    private StopCondition stopCondition;

    private LineSearch lineSearch;

    public SteepestDescentMinimization(ObjectiveFunction ofunc) {
        this(ofunc, LineSearchFactory.createDefault(ofunc));
    }

    private SteepestDescentMinimization(ObjectiveFunction ofunc, LineSearch olineSearch) {
        this(ofunc, olineSearch, MachineAccuracy.SQRT_EPSILON, 20000);
    }

    public SteepestDescentMinimization(ObjectiveFunction ofunc, LineSearch olineSearch, final double otolerance, final int omaxIterations) {
        func = ofunc;
        n = ofunc.getNumArguments();
        lineSearch = olineSearch;
        maxIterations = omaxIterations;
        stopCondition = new PALStopCondition(otolerance);
    }

    public double getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(double maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void minimize(double[] ox) throws SteepestDescentMinimizationException, LineSearchException {
        fireOptimizationStart();
        double[] g = new double[n];
        double[] p = new double[n];
        double[] pold = new double[n];
        x = ox;
        if (func.isAnalyticGradient()) {
            fx = func.evaluate(x, g);
        } else {
            fx = func.evaluate(x);
            NumericalDifferentiation.gradientCD(func, x, g);
        }
        stopCondition.init(fx, x);
        for (iteration = 1; iteration <= maxIterations; iteration++) {
            fireIterationStart();
            for (int i = 0; i < n; i++) {
                p[i] = -g[i];
            }
            fx = lineSearch.minimize(x, p, fx, g);
            if (stopCondition.stop(fx, x)) {
                fireOptimizationEnd();
                return;
            }
            fireIterationEnd();
        }
        throw new SteepestDescentMinimizationException("Too many iterations.");
    }
}
