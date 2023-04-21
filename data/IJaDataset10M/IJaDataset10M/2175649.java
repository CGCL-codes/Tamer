package net.sf.doodleproject.numerics4j.statistics.distribution;

import net.sf.doodleproject.numerics4j.exception.NumericException;

/**
 * <p>
 * The Chi-Squared distribution (1).
 * </p>
 * <p>
 * <ol>
 * <li> Eric W. Weisstein. "Chi-Squared Distribution." From MathWorld--A Wolfram
 * Web Resource. <a target="_blank"
 * href="http://mathworld.wolfram.com/Chi-SquaredDistribution.html">
 * http://mathworld.wolfram.com/Chi-SquaredDistribution.html</a> </li>
 * </ol>
 * </p>
 * 
 * @version $Revision: 1.2 $ $Date: 2007/10/25 04:44:10 $
 */
public class ChiSquaredDistribution extends ContinuousDistribution {

    /** Internal gamma distribution. */
    private GammaDistribution gamma;

    /**
     * Default constructor. Degrees of freedom is set to 1.
     */
    public ChiSquaredDistribution() {
        this(1.0);
    }

    /**
     * Create a distribution with the given degrees of freedom.
     * 
     * @param df degrees of freedom.
     */
    public ChiSquaredDistribution(double df) {
        super();
        setGamma(new GammaDistribution(df / 2.0, 2.0));
    }

    /**
     * The CDF for this distribution. This method returns P(X &lt; x).
     * 
     * @param x the value at which the CDF is evaluated.
     * @return CDF for this distribution.
     * @throws NumericException if the cumulative probability can not be
     *         computed.
     */
    public double cumulativeProbability(double x) throws NumericException {
        return getGamma().cumulativeProbability(x);
    }

    /**
     * Access the degrees of freedom.
     * 
     * @return the degrees of freedom.
     */
    public double getDegreesOfFreedom() {
        return getGamma().getAlpha() * 2.0;
    }

    /**
     * Access the internal gamma distribution.
     * 
     * @return the gamma distribution.
     */
    private GammaDistribution getGamma() {
        return gamma;
    }

    /**
     * The inverse CDF for this distribution. This method returns x such that,
     * P(X &lt; x) = p.
     * 
     * @param p the cumulative probability.
     * @return x
     * @throws NumericException if the inverse cumulative probability can not be
     *         computed.
     */
    public double inverseCumulativeProbability(double p) throws NumericException {
        return getGamma().inverseCumulativeProbability(p);
    }

    /**
     * Modify the degrees of freedom.
     * 
     * @param df The new degrees of freedom value.
     */
    public void setDegreesOfFreedom(double df) {
        getGamma().setAlpha(df / 2.0);
    }

    /**
     * Modify the internal gamma distribution.
     * 
     * @param g the new internal gamma distribution.
     */
    private void setGamma(GammaDistribution g) {
        this.gamma = g;
    }
}
