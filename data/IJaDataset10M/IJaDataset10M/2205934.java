package com.rapidminer.operator.learner.rules;

/**
 * Contains all information about a numerical split point.
 *
 * @author Ingo Mierswa
 */
public class Split {

    public static final int LESS_SPLIT = 0;

    public static final int GREATER_SPLIT = 1;

    private double splitPoint;

    private double[] benefit;

    private int splitType;

    public Split(double splitPoint, double[] benefit, int splitType) {
        this.splitPoint = splitPoint;
        this.benefit = benefit;
        this.splitType = splitType;
    }

    public double getSplitPoint() {
        return this.splitPoint;
    }

    public double[] getBenefit() {
        return this.benefit;
    }

    public int getSplitType() {
        return splitType;
    }
}
