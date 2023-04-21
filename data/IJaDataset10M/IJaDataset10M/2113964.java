package com.rapidminer.tools.math.smoothing;

/**
 * 
 * This class implements a McLain smoothing kernel with fixed epsilon on 1 for
 * normalization, so that it returns a weight of 1 on distance 0.
 * @author Sebastian Land
 *
 */
public class McLainSmoothingKernel extends SmoothingKernel {

    private static final long serialVersionUID = -5396004335809012646L;

    @Override
    public double getWeight(double distance) {
        double toSquare = distance + 1;
        return 1d / (toSquare * toSquare);
    }

    @Override
    public String toString() {
        return "McLain Smoothing Kernel";
    }
}
