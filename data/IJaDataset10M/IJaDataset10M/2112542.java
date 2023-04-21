package org.personalsmartspace.lm.mining.sequence;

public class Probability {

    private double probability;

    public Probability(double p) {
        if (p < 0.0 || p > 1.0) {
            System.err.println(p + " is not a valid probability. Exiting!");
            System.exit(-1);
        }
        this.probability = p;
    }

    public double getValue() {
        return this.probability;
    }
}
