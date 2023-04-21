package com.rapidminer.tools.math.kernels;

import com.rapidminer.tools.Tools;

/**
 * Returns the value of the Anova (RBF) kernel of both examples.
 * 
 * @author Ingo Mierswa
 */
public class AnovaKernel extends Kernel {

    private static final long serialVersionUID = -2955083072613762504L;

    /** The parameter gamma of the Anova kernel. */
    private double gamma = -1.0d;

    /** The parameter degree of the Anova kernel. */
    private double degree;

    @Override
    public int getType() {
        return KERNEL_ANOVA;
    }

    public void setGamma(double gamma) {
        this.gamma = -gamma;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public double getGamma() {
        return -gamma;
    }

    /** Calculates kernel value of vectors x and y. */
    @Override
    public double calculateDistance(double[] x1, double[] x2) {
        double result = 0;
        int zeros = x1.length;
        for (int i = 0; i < x1.length; i++) {
            double factor = x1[i] - x2[i];
            result += Math.exp(gamma * factor * factor);
            if (Tools.isNotEqual(x1[i], 0.0d) || Tools.isNotEqual(x2[i], 0.0d)) {
                zeros--;
            }
        }
        result += zeros;
        return Math.pow(result, degree);
    }

    @Override
    public String getDistanceFormula(double[] x, String[] attributeConstructions) {
        StringBuffer result = new StringBuffer();
        result.append("pow((");
        boolean first = true;
        for (int i = 0; i < x.length; i++) {
            double value = x[i];
            String valueString = "(" + value + " - " + attributeConstructions[i] + ")";
            if (first) {
                result.append("exp(-" + Math.abs(gamma) + " * " + valueString + " * " + valueString + ")");
            } else {
                result.append(" + exp(-" + Math.abs(gamma) + " * " + valueString + " * " + valueString + ")");
            }
            first = false;
        }
        result.append("), " + degree + ")");
        return result.toString();
    }

    @Override
    public String toString() {
        return "Anova Kernel with" + Tools.getLineSeparator() + "  gamma: " + Tools.formatNumber(getGamma()) + Tools.getLineSeparator() + "  degree: " + degree;
    }
}
