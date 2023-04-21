package net.sf.mzmine.parameters.parametertypes;

import net.sf.mzmine.util.Range;

/**
 * This class represents m/z tolerance. Tolerance is set using absolute (m/z)
 * and relative (ppm) values. The tolerance range is calculated as the maximum
 * of the absolute and relative values.
 */
public class MZTolerance {

    private final double mzTolerance, ppmTolerance;

    public MZTolerance(double mzTolerance, double ppmTolerance) {
        this.mzTolerance = mzTolerance;
        this.ppmTolerance = ppmTolerance;
    }

    public double getMzTolerance() {
        return mzTolerance;
    }

    public double getPpmTolerance() {
        return ppmTolerance;
    }

    public double getMzToleranceForMass(double mzValue) {
        double calculatedMzTolerance = Math.max(mzTolerance, (mzValue / 1000000d * ppmTolerance));
        return calculatedMzTolerance;
    }

    public double getPpmToleranceForMass(double mzValue) {
        double calculatedPpmTolerance = Math.max(mzTolerance / (mzValue / 1000000d), ppmTolerance);
        return calculatedPpmTolerance;
    }

    public Range getToleranceRange(double mzValue) {
        double absoluteTolerance = Math.max(mzTolerance, (mzValue / 1000000 * ppmTolerance));
        return new Range(mzValue - absoluteTolerance, mzValue + absoluteTolerance);
    }

    public Range getToleranceRange(Range mzRange) {
        double absoluteMinTolerance = Math.max(mzTolerance, (mzRange.getMin() / 1000000 * ppmTolerance));
        double absoluteMaxTolerance = Math.max(mzTolerance, (mzRange.getMax() / 1000000 * ppmTolerance));
        return new Range(mzRange.getMin() - absoluteMinTolerance, mzRange.getMax() + absoluteMaxTolerance);
    }

    public boolean checkWithinTolerance(double mz1, double mz2) {
        Range toleranceRange = getToleranceRange(mz1);
        return toleranceRange.contains(mz2);
    }
}
