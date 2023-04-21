package net.sf.mzmine.modules.masslistmethods.shoulderpeaksfilter.peakmodels;

import net.sf.mzmine.modules.rawdatamethods.peakpicking.massdetection.exactmass.PeakModel;
import net.sf.mzmine.util.Range;

/**
 * 
 * This class represents a Lorentzian function model, using the formula:
 * 
 * f(x) = a / (1 + ((x-b)^2 / (HWHM^2)))
 * 
 * where
 * 
 * a... height of the model (intensityMain) b... center of the model (mzMain)
 * HWHM... Half Width at Half Maximum
 * 
 */
public class LorentzianPeak implements PeakModel {

    private double mzMain, intensityMain, squareHWHM;

    /**
     * @see net.sf.mzmine.modules.peakpicking.twostep.massdetection.exactmass.peakmodel.PeakModel#setParameters(double,
     *      double, double)
     */
    public void setParameters(double mzMain, double intensityMain, double resolution) {
        this.mzMain = mzMain;
        this.intensityMain = intensityMain;
        squareHWHM = (double) Math.pow((mzMain / resolution) / 2, 2);
    }

    /**
     * @see net.sf.mzmine.modules.peakpicking.twostep.peakmodel.PeakModel#getBasePeakWidth()
     */
    public Range getWidth(double partialIntensity) {
        if (partialIntensity <= 0) return new Range(0, Double.MAX_VALUE);
        double squareX = ((intensityMain / partialIntensity) - 1) * squareHWHM;
        double sideRange = (double) Math.sqrt(squareX);
        Range rangePeak = new Range(mzMain - sideRange, mzMain + sideRange);
        return rangePeak;
    }

    /**
     * @see net.sf.mzmine.modules.peakpicking.twostep.peakmodel.PeakModel#getIntensity(double)
     */
    public double getIntensity(double mz) {
        double squareX = (double) Math.pow((mz - mzMain), 2);
        double ratio = squareX / squareHWHM;
        double intensity = intensityMain / (1 + ratio);
        return intensity;
    }
}
