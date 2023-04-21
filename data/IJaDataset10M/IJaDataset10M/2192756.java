package net.sourceforge.piqle.neuralnetwork.nu.optimization.impl;

public class RProp implements SingleValueOptimizer {

    private double increaseFactor = 1.2;

    private double decreaseFactor = 0.5;

    private double minUpdateValue = 0.0000001;

    private double maxUpdateValue = 50;

    private double oldGradient;

    private double updateValues = 0.1;

    public double computeWeightChange(double negGrad) {
        int sign = (int) Math.signum(negGrad * oldGradient);
        if (sign > 0) {
            updateValues = Math.min(increaseFactor * updateValues, maxUpdateValue);
        } else if (sign < 0) {
            updateValues = Math.max(decreaseFactor * updateValues, minUpdateValue);
        }
        double delta = Math.signum(negGrad) * updateValues;
        oldGradient = calculateSearchDirection(negGrad);
        return delta;
    }

    protected double calculateSearchDirection(double negGrad) {
        return negGrad;
    }
}
