package edu.ucla.stat.SOCR.distributions;

import edu.ucla.stat.SOCR.core.*;

/**
 * This class models the distribution of the maximum value of a symmetric random
 * walk on the interval [0, n].
 */
public class WalkMaxDistribution extends Distribution {

    private int steps;

    /**
     * This general constructor creates a new max walk distribution with a
     * specified time parameter.
     */
    public WalkMaxDistribution(int n) {
        setSteps(n);
    }

    /**
     * This default constructor creates a new walk max distribution with time
     * parameter 10.
     */
    public WalkMaxDistribution() {
        this(10);
    }

    /**
     * This method sets the time parameter.
     *
     * @uml.property name="steps"
     */
    public void setSteps(int n) {
        if (n < 1) n = 1;
        steps = n;
        super.setParameters(0, steps, 1, DISCRETE);
        name = "Walk Max (" + steps + ") Distribution";
    }

    /** This method defines the density function. */
    public double getDensity(double x) {
        int k = (int) Math.rint(x), m;
        if ((k + steps) % 2 == 0) m = (k + steps) / 2; else m = (k + steps + 1) / 2;
        return comb(steps, m) / Math.pow(2, steps);
    }

    /** This method returns the maximum value of the density function. */
    public double getMaxDensity() {
        return getDensity(0);
    }

    /** This method returns the number ofsteps. */
    public double getSteps() {
        return steps;
    }

    /** This method simulates a value from the distribution. */
    public double simulate() {
        int step, max = 0, position = 0;
        for (int i = 1; i <= steps; i++) {
            if (Math.random() < 0.5) step = 1; else step = -1;
            position = position + step;
            if (position > max) max = position;
        }
        return max;
    }

    /** This method returns an online description of this distribution. */
    public String getOnlineDescription() {
        return new String("http://planetmath.org/encyclopedia/SymmetricSimpleRandomWalk.html");
    }
}
