package pspdash;

import DistLib.uniform;

public interface ConfidenceInterval {

    /** Some confidence intervals are sensitive to the value they are
     * correcting.  Clients should call setInput() with the input value
     * before calling other methods on the confidence interval.
     */
    public void setInput(double input);

    /** Return the forecast value produced by translating the input
     * value according to correction algorithm contained in this
     * confidence interval.
     */
    public double getPrediction();

    /** Return the lower end of the confidence interval for a
     * particular confidence percentage.
     */
    public double getLPI(double percentage);

    /** Return the upper end of the confidence interval for a
     * particular confidence percentage.
     */
    public double getUPI(double percentage);

    /** Return a random value from the distribution upon which
     * this confidence interval is based.
     */
    public double getRandomValue(uniform u);

    /** Return a value indicating how viable this confidence interval
     * seems. Numbers less than 0 indicate invalid confidence
     * intervals which should not be used for planning purposes.
     */
    public double getViability();

    public final double CANNOT_CALCULATE = -100.0;

    public final double SERIOUS_PROBLEM = -10.0;

    public final double ACCEPTABLE = 0.0;

    public final double NOMINAL = 5.0;
}
