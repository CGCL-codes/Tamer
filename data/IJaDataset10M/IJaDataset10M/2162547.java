package edu.ucla.stat.SOCR.distributions;

import edu.ucla.stat.SOCR.core.*;

/**
 * This class models the Circle distribution with parameter a (radius). This is
 * the distribution of X and Y when (X, Y) has the uniform distribution on a
 * circular region with a specified radius.
 */
public class CircleDistribution extends Distribution {

    /**
     * @uml.property name="radius"
     */
    private double radius;

    /**
     * This general constructor creates a new circle distribution with a
     * specified radius.
     */
    public CircleDistribution(double r) {
        setRadius(r);
    }

    /** This special constructor creates a new circle distribution with radius 1 */
    public CircleDistribution() {
        this(1);
        name = "Circle Distribution";
    }

    public void initialize() {
        createValueSetter("Radius", CONTINUOUS, 0, 10);
    }

    public void valueChanged() {
        setRadius(getValueSetter(0).getValue());
    }

    /**
     * This method sets the radius parameter
     *
     * @uml.property name="radius"
     */
    public void setRadius(double r) {
        if (r <= 0) r = 1;
        radius = r;
        super.setParameters(-radius, radius, 0.02 * radius, CONTINUOUS);
    }

    /** This method computes the getDensity function. */
    public double getDensity(double x) {
        if (-radius <= x & x <= radius) return 2 * Math.sqrt(radius * radius - x * x) / (Math.PI * radius * radius); else return 0;
    }

    /** This method computes the maximum value of the getDensity function. */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /** This method computes the mean */
    public double getMean() {
        return 0;
    }

    /** This method computes the variance */
    public double getVariance() {
        return radius * radius / 4;
    }

    /** This method computes the median. */
    public double getMedian() {
        return 0;
    }

    /**
     * This method returns the radius parameter.
     *
     * @uml.property name="radius"
     */
    public double getRadius() {
        return radius;
    }

    /** This method simulates a value from the distribution. */
    public double simulate() {
        double u = radius * Math.random();
        double v = radius * Math.random();
        double r = Math.max(u, v);
        double theta = 2 * Math.PI * Math.random();
        return r * Math.cos(theta);
    }

    /** This method compute the cumulative distribution function. */
    public double getCDF(double x) {
        return 0.5 + Math.asin(x / radius) / Math.PI + x * Math.sqrt(1 - x * x / (radius * radius)) / (Math.PI * radius);
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://en.wikipedia.org/wiki/Half_circle_distribution");
    }
}
