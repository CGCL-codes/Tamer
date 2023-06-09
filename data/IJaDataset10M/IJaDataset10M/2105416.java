package edu.uci.ics.jung.algorithms;

import edu.uci.ics.jung.utils.NumericalPrecision;

/**
 * Provides basic infrastructure for iterative algorithms. Services provided include:
 * <ul>
 * <li> storage of current and max iteration count </li>
 * <li> framework for initialization, iterative evaluation, and finalization </li>
 * <li> test for convergence </li>
 * <li> etc. </li>
 * </ul>
 * <p>
 * Algorithms that subclass this class are typically used in the following way: <br>
 * <pre>
 * FooAlgorithm foo = new FooAlgorithm(...)
 * foo.setMaximumIterations(100); //set up conditions
 * ...
 * foo.evaluate(); //key method which initiates iterative process
 * foo.getSomeResult();
 * </pre>
 * 
 * @author Scott White (originally written by Didier Besset)
 */
public abstract class IterativeProcess {

    /**
     * Number of iterations performed.
     */
    private int iterations;

    /**
     * Maximum allowed number of iterations.
     */
    private int maximumIterations = 50;

    /**
     * Desired precision.
     */
    private double desiredPrecision = NumericalPrecision.defaultNumericalPrecision();

    /**
     * Achieved precision.
     */
    private double precision;

    /**
     * Generic constructor.
     */
    public IterativeProcess() {
    }

    /**
     * Performs the iterative process.
     * Note: this method does not return anything because Java does not
     * allow mixing double, int, or objects
     */
    public void evaluate() {
        iterations = 0;
        initializeIterations();
        while (iterations++ < maximumIterations) {
            precision = evaluateIteration();
            if (hasConverged()) break;
        }
        finalizeIterations();
    }

    /**
     * Evaluate the result of the current interation.
     * @return the estimated precision of the result.
     */
    protected abstract double evaluateIteration();

    /**
     * Perform eventual clean-up operations
     * (must be implement by subclass when needed).
     */
    protected void finalizeIterations() {
    }

    /**
     * Returns the desired precision.
     */
    public double getDesiredPrecision() {
        return desiredPrecision;
    }

    /**
     * Returns the number of iterations performed.
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Returns the maximum allowed number of iterations.
     */
    public int getMaximumIterations() {
        return maximumIterations;
    }

    /**
     * Returns the attained precision.
     */
    public double getPrecision() {
        return precision;
    }

    /**
     *
     * Check to see if the result has been attained.
     * @return boolean
     */
    public boolean hasConverged() {
        return precision < desiredPrecision;
    }

    /**
     * Initializes internal parameters to start the iterative process.
     */
    protected void initializeIterations() {
    }

    protected void reinitialize() {
    }

    /**
     * @return double
     * @param epsilon double
     * @param x double
     */
    public double relativePrecision(double epsilon, double x) {
        return x > NumericalPrecision.defaultNumericalPrecision() ? epsilon / x : epsilon;
    }

    /**
     * Defines the desired precision.
     */
    public void setDesiredPrecision(double prec) throws IllegalArgumentException {
        if (prec <= 0) throw new IllegalArgumentException("Non-positive precision: " + prec);
        desiredPrecision = prec;
    }

    /**
     * Defines the maximum allowed number of iterations.
     */
    public void setMaximumIterations(int maxIter) throws IllegalArgumentException {
        if (maxIter < 1) throw new IllegalArgumentException("Non-positive maximum iteration: " + maxIter);
        maximumIterations = maxIter;
    }
}
