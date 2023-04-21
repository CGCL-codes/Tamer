package game.trainers;

import game.trainers.gradient.Newton.Uncmin_methods;
import game.trainers.random.RandomSearch;
import game.models.GradientTrainable;
import configuration.models.game.trainers.RandomConfig;

public class RandomTrainer extends Trainer implements Uncmin_methods {

    private static final long serialVersionUID = 1L;

    int cnt;

    private transient RandomSearch randomSearch;

    private int maxIterations;

    private int maxStagnation;

    private boolean debugOn;

    private double min, max;

    private double gradientWeight;

    private int cycle;

    public void init(GradientTrainable uni, Object cfg) {
        super.init(uni, cfg);
        RandomConfig cf = (RandomConfig) cfg;
        maxIterations = cf.getMaxIterations();
        maxStagnation = cf.getMaxStagnation();
        min = cf.getMin();
        max = cf.getMax();
        gradientWeight = cf.getGradientWeight();
        cycle = cf.getCycle();
        debugOn = cf.getDebugOn();
    }

    public void setCoef(int coef) {
        super.setCoef(coef);
        randomSearch = new RandomSearch(this, coefficients, maxIterations, maxStagnation, min, max, gradientWeight, cycle, debugOn);
    }

    /** starts the teaching process */
    public void teach() {
        randomSearch.run();
    }

    /** returns the name of the algorithm used for weights(coeffs.) estimation */
    public String getMethodName() {
        return "Random Search Algorithm";
    }

    /** returns error of vector x */
    public double f_to_minimize(double[] x) {
        return getAndRecordError(x, 10, 100, true);
    }

    public void gradient(double[] x, double[] g) {
        unit.gradient(x, g);
    }

    public void hessian(double[] x, double[][] h) {
        unit.hessian(x, h);
    }

    public boolean allowedByDefault() {
        return false;
    }

    public Class getConfigClass() {
        return RandomConfig.class;
    }

    public boolean isExecutableInParallelMode() {
        return true;
    }
}
