package com.breaktrycatch.needmorehumans.util.math.kernels;

public class EpanechnikovKernel extends Kernel {

    /** The parameter sigma of the Epanechnikov kernel. */
    private double sigma = 1.0d;

    /** The parameter degree of the Epanechnikov kernel. */
    private double degree = 1;

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getSigma() {
        return sigma;
    }

    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    /** Calculates kernel value of vectors x and y. */
    public double calculateDistance(double[] x1, double[] x2) {
        double expression = norm2(x1, x2) / sigma;
        if (expression > 1) return 0.0d; else {
            double minus = 1.0d - expression;
            return Math.pow(minus, degree);
        }
    }
}
