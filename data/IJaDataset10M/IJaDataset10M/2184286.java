package com.rapidminer.tools.math.smoothing;

/**
 * This class implements the bisquare smoothing kernel.
 * @author Sebastian Land
 */
public class BisquareSmoothingKernel extends SmoothingKernel {

    private static final long serialVersionUID = 3789772699936743816L;

    @Override
    public double getWeight(double distance) {
        if (distance <= 1) {
            double toSquare = 1 - distance * distance;
            return toSquare * toSquare;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Bisquare Smoothing Kernel";
    }
}
