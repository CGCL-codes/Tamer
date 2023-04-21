package guineu.parameters.parametersType;

import guineu.util.Range;

/**
 * 
 */
public class MZTolerance {

    private boolean isAbsolute;

    private double tolerance;

    public MZTolerance(boolean isAbsolute, double tolerance) {
        this.isAbsolute = isAbsolute;
        this.tolerance = tolerance;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }

    public double getTolerance() {
        return tolerance;
    }

    public Range getToleranceRange(double mzValue) {
        double absoluteTolerance = isAbsolute ? tolerance : (mzValue / 1000000 * tolerance);
        return new Range(mzValue - absoluteTolerance, mzValue + absoluteTolerance);
    }

    public boolean checkWithinTolerance(double mz1, double mz2) {
        Range toleranceRange = getToleranceRange(mz1);
        return toleranceRange.contains(mz2);
    }
}
