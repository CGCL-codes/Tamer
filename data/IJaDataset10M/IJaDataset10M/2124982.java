package edu.ucla.stat.SOCR.distributions;

import edu.ucla.stat.SOCR.core.*;
import edu.ucla.stat.SOCR.distributions.exception.ParameterOutOfBoundsException;

/**
 * Gamma distribution with a specified shape parameter and scale parameter. <a
 * href="http://en.wikipedia.org/wiki/Gamma_distribution">
 * http://en.wikipedia.org/wiki/Gamma_distribution </a>.
 */
public class GammaDistribution extends Distribution {

    private double shape;

    private double scale;

    private double c;

    public GammaDistribution(double[] distData) {
        paramEstimate(distData);
    }

    public GammaDistribution(float[] distData) {
        double[] rData = new double[distData.length];
        for (int i = 0; i < distData.length; i++) rData[i] = (double) distData[i];
        paramEstimate(rData);
    }

    /**
     * General Constructor: creates a new gamma distribution with shape
     * parameter k and scale parameter b
     */
    public GammaDistribution(double k, double b) {
        setParameters(k, b);
    }

    /**
     * Default Constructor: creates a new gamma distribution with shape
     * parameter 1 and scale parameter 1
     */
    public GammaDistribution() {
        this(1, 1);
        name = "Gamma Distribution";
    }

    public void initialize() {
        createValueSetter("Shape", CONTINUOUS, 1, 41);
        createValueSetter("Scale", CONTINUOUS, 1, 41);
    }

    public void valueChanged() {
        double v1 = getValueSetter(0).getValue();
        double v2 = getValueSetter(1).getValue();
        setParameters(v1, v2);
    }

    /** Set parameters and assign the default partition */
    public void setParameters(double k, double b) {
        double upperBound;
        if (k < 0) k = 1;
        if (b < 0) b = 1;
        shape = k;
        scale = b;
        c = shape * Math.log(scale) + logGamma(shape);
        upperBound = getMean() + 4 * getSD();
        super.setParameters(0, upperBound, 0.01 * upperBound, CONTINUOUS);
        super.setMGFParameters(0, 1.0 / scale, 1000.0);
    }

    /**
     * Get shape parameters
     *
     * @uml.property name="shape"
     */
    public double getShape() {
        return shape;
    }

    /**
     * Get scale parameters
     *
     * @uml.property name="scale"
     */
    public double getScale() {
        return scale;
    }

    /** Density function */
    public double getDensity(double x) {
        if (x < 0) return 0; else if (x == 0 & shape < 1) return Double.POSITIVE_INFINITY; else if (x == 0 & shape == 1) return Math.exp(-c); else if (x == 0 & shape > 1) return 0; else return Math.exp(-c + (shape - 1) * Math.log(x) - x / scale);
    }

    /** Maximum value of getDensity function */
    public double getMaxDensity() {
        double mode;
        if (shape < 1) mode = 0.01; else mode = scale * (shape - 1);
        return getDensity(mode);
    }

    /** Mean */
    public double getMean() {
        return shape * scale;
    }

    /** Variance */
    public double getVariance() {
        return shape * scale * scale;
    }

    /** Cumulative distribution function */
    public double getCDF(double x) {
        return gammaCDF(x / scale, shape);
    }

    /** Computes the moment generating function in closed form for a
     * parameter t which lies in the domain of the distribution. */
    public double getMGF(double t) throws ParameterOutOfBoundsException {
        if (t >= 1 / scale) throw new ParameterOutOfBoundsException("Parameter t must be less than 1/scale"); else return Math.pow(1 - scale * t, -shape);
    }

    /** Simulate a value */
    public double simulate() {
        System.out.println("GammaDistribution simulate shape = " + shape);
        if (shape == Math.rint(shape)) {
            double sum = 0;
            for (int i = 1; i <= shape; i++) {
                sum = sum - scale * Math.log(1 - Math.random());
            }
            return sum;
        } else return super.simulate();
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Gamma_distribution");
    }

    /**
     * @uml.property name="shape"
     */
    public void paramEstimate(double[] distData) {
        double sMean = sampleMean(distData);
        double sVar = sampleVar(distData, sMean);
        scale = sVar / sMean;
        shape = sMean / scale;
        setParameters(scale, shape);
    }
}
