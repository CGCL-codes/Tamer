package cma;

import cma.ISolutionPoint;

/** solution point in search space. Rather plain implementation of the interface ISolutionPoint. 
 * 
 * @see ISolutionPoint
 * */
public class CMASolution implements ISolutionPoint, java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6257830429350615236L;

    public CMASolution() {
    }

    public CMASolution(double[] x, double fitnessValue, long evaluation) {
        this.functionValue = fitnessValue;
        this.x = x.clone();
        this.evaluation = evaluation;
    }

    public CMASolution deepCopy() {
        return new CMASolution(x, functionValue, evaluation);
    }

    public CMASolution(double[] x) {
        this.x = x;
    }

    public double getFitness() {
        return functionValue;
    }

    public long getEvaluationNumber() {
        return evaluation;
    }

    public double[] getX() {
        return x.clone();
    }

    public void setFitness(double f) {
        functionValue = f;
    }

    public void setEvaluationNumber(long e) {
        evaluation = e;
    }

    public void setX(double[] x_in) {
        x = new double[x_in.length];
        for (int i = 0; i < x.length; ++i) x[i] = x_in[i];
    }

    /** objective function value of x */
    private double functionValue = Double.NaN;

    /** argument to objective function to be optimized */
    private double[] x;

    /** count when the solution was evaluated */
    private long evaluation = 0;
}
