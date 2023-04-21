package de.ibis.permoto.solver.sim.tech.simEngine1.dataAnalysis;

/**
 * <p>Title: DynamicDataAnalyzer Interface</p>
 * <p>Description: This interface is implemented by Data Analyzers used to evaluate
 * confidence intervals and to detect and discard initial transient phase.</p>
 *
 * @author Bertoli Marco
 *         Date: 11-gen-2006
 *         Time: 10.40.52
 */
public interface DynamicDataAnalyzer {

    /**
     * Returns if the analysis was succesful
     * @return true if the analysis respect all'users requests
     */
    public boolean getSuccess();

    /**
     * Gets the mean estimate of the monitored data stream
     * @return mean estimate of the monitored data stream
     */
    public double getMean();

    public double getVar();

    /**
     * Gets the extimated mean, even if the confidence requirements
     * haven't been reached
     * @return the extimated mean
     */
    public double extimatedMean();

    /**
     * Returns true if null test has found out that this measure is zero
     * @return true if null test has found out that this measure is zero
     */
    public boolean isZero();

    /**
     * Gets the half width confidence interval extimated of the monitored data
     * stream.
     * @return the confidence interval requested
     */
    public double getConfInt();

    /**
     * This method should be called only if mean is equal to zero (@see isZero()), that is
     * whne nullTest() returns true.
     * @return the upper limit of confidence interval when mean is equal to 0.0
     */
    public double getNullMeasure_upperLimit();

    /**
     * Gets number of samples elaborated up to now
     * @return number of samples elaborated
     */
    public int getSamples();

    /**
     * Gets the number of discarded data
     * @return the number of discarded data
     */
    public int getDiscarded();

    /**
     * Gets the maximum number of samples that can be analyzed
     * @return maximum number of samples that can be analyzed
     */
    public int getMaxData();

    /**
     * Adds the new sample to the statistic.
     * @param newSample the new sample
     * @param weight the weight of the newSample, if it is not needed put 1.
     * @return true if the computation of confidence interval has finished (i.e. if the
     * confidence interval is smaller than the one required by
     * the user, or if the analyzed data are too many), false otherwise
     */
    public boolean addSample(double newSample, double weight);

    /**
     * Gets measure precision (i.e. Maximum relative error)
     * @return precision value
     */
    public double getPrecision();

    /**
     * Gets measure alpha (i.e. confidence interval)
     * @return alpha value
     */
    public double getAlfa();

    /**
     * Stops the measuring process
     * @param success value to be reported by getSuccess()
     */
    public void stopMeasure(boolean success);

    /**
     * Sets the reference to the class containing simulation
     * parameters. The parameters are updated with the passed values.
     *
     * @param parameters
     */
    public void setParameters(SimParameters parameters);
}
