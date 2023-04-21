package net.sf.doodleproject.numerics4j.random;

/**
 * <p>
 * A random variable generator for Student's t distribution.
 * </p>
 * <p>
 * References:
 * <ol>
 * <li>Wikipedia contributors, "Student's t-Distribution," Wikipedia, The Free
 * Encyclopedia, <a target="_blank"
 * href="http://en.wikipedia.org/wiki/T_distribution">
 * http://en.wikipedia.org/wiki/T_distribution</a></li>
 * </ol>
 * </p>
 * 
 * @since 1.3
 * @version $Revision: 1.3 $ $Date: 2007/11/18 23:51:19 $
 */
public class TRandomVariable extends AbstractContinuousRandomVariable {

    /** The degreesOfFreedom. */
    private double degreesOfFreedom;

    /**
     * Default constructor. Degrees of freedom is set to 1.
     */
    public TRandomVariable() {
        this(1.0);
    }

    /**
     * Create a random variable with the given degrees of freedom.
     * 
     * @param df degrees of freedom.
     */
    public TRandomVariable(double df) {
        this(df, new RandomRNG());
    }

    /**
     * Create a random variable with the given parameters.
     * 
     * @param df degrees of freedom.
     * @param source the source generator.
     */
    public TRandomVariable(double df, RNG source) {
        super(source);
        setDegreesOfFreedom(df);
    }

    /**
     * Access the next random variable using the given generator.
     * 
     * @param df degrees of freedom.
     * @param source the source generator.
     * @return the next random variable.
     */
    public static double nextRandomVariable(double df, RNG source) {
        double x;
        double v;
        do {
            double u0 = source.nextRandomNumber();
            double u1 = source.nextRandomNumber();
            if (u0 < 0.5) {
                x = 1.0 / (4.0 * u0 - 1.0);
                v = u1 / (x * x);
            } else {
                x = 4.0 * u0 - 3.0;
                v = u1;
            }
        } while (v >= 1.0 - 0.5 * Math.abs(x) && v >= Math.pow(1.0 + (x * x) / df, -0.5 * (df + 1.0)));
        return x;
    }

    /**
     * Access the degrees of freedom.
     * 
     * @return the degrees of freedom.
     */
    private double getDegreesOfFreedom() {
        return degreesOfFreedom;
    }

    /**
     * Access the next random variable from this generator.
     * 
     * @return the next random variable.
     */
    public double nextRandomVariable() {
        return nextRandomVariable(getDegreesOfFreedom(), getSource());
    }

    /**
     * Modify the degrees of freedom.
     * 
     * @param df the new degrees of freedom value.
     */
    private void setDegreesOfFreedom(double df) {
        if (df <= 0.0 || Double.isNaN(df)) {
            throw new IllegalArgumentException("Degrees of freedom must be positive.");
        }
        this.degreesOfFreedom = df;
    }
}
