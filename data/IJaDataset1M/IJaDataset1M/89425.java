package net.sf.mzmine.modules.peaklistmethods.peakpicking.deconvolution;

import java.util.Arrays;
import net.sf.mzmine.data.ChromatographicPeak;
import net.sf.mzmine.data.DataPoint;
import net.sf.mzmine.data.IsotopePattern;
import net.sf.mzmine.data.PeakStatus;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.data.Scan;
import net.sf.mzmine.data.impl.SimpleDataPoint;
import net.sf.mzmine.util.MathUtils;
import net.sf.mzmine.util.PeakUtils;
import net.sf.mzmine.util.Range;
import net.sf.mzmine.util.ScanUtils;

/**
 * ResolvedPeak
 * 
 */
public class ResolvedPeak implements ChromatographicPeak {

    private RawDataFile dataFile;

    private double mz, rt, height, area;

    private int scanNumbers[];

    private double dataPointMZValues[], dataPointIntensityValues[];

    private int representativeScan, fragmentScan;

    private Range rawDataPointsIntensityRange, rawDataPointsMZRange, rawDataPointsRTRange;

    private IsotopePattern isotopePattern = null;

    private int charge = 0;

    /**
     * Initializes this peak using data points from a given chromatogram -
     * regionStart marks the index of the first data point (inclusive),
     * regionEnd marks the index of the last data point (inclusive). The
     * selected region MUST NOT contain any zero-intensity data points,
     * otherwise exception is thrown.
     */
    public ResolvedPeak(ChromatographicPeak chromatogram, int regionStart, int regionEnd) {
        assert regionEnd > regionStart;
        this.dataFile = chromatogram.getDataFile();
        scanNumbers = new int[regionEnd - regionStart + 1];
        int chromatogramScanNumbers[] = chromatogram.getDataFile().getScanNumbers(1);
        System.arraycopy(chromatogramScanNumbers, regionStart, scanNumbers, 0, regionEnd - regionStart + 1);
        dataPointMZValues = new double[regionEnd - regionStart + 1];
        dataPointIntensityValues = new double[regionEnd - regionStart + 1];
        height = Double.MIN_VALUE;
        for (int i = 0; i < scanNumbers.length; i++) {
            DataPoint dp = chromatogram.getDataPoint(scanNumbers[i]);
            if (dp == null) {
                String error = "Cannot create a resolved peak in a region with missing data points: chromatogram " + chromatogram + " scans " + chromatogramScanNumbers[regionStart] + "-" + chromatogramScanNumbers[regionEnd] + ", missing data point in scan " + scanNumbers[i];
                throw new IllegalArgumentException(error);
            }
            dataPointMZValues[i] = dp.getMZ();
            dataPointIntensityValues[i] = dp.getIntensity();
            if (rawDataPointsIntensityRange == null) {
                rawDataPointsIntensityRange = new Range(dp.getIntensity());
                rawDataPointsRTRange = new Range(dataFile.getScan(scanNumbers[i]).getRetentionTime());
                rawDataPointsMZRange = new Range(dp.getMZ());
            } else {
                rawDataPointsRTRange.extendRange(dataFile.getScan(scanNumbers[i]).getRetentionTime());
                rawDataPointsIntensityRange.extendRange(dp.getIntensity());
                rawDataPointsMZRange.extendRange(dp.getMZ());
            }
            if (height < dp.getIntensity()) {
                height = dp.getIntensity();
                rt = dataFile.getScan(scanNumbers[i]).getRetentionTime();
                representativeScan = scanNumbers[i];
            }
        }
        mz = MathUtils.calcQuantile(dataPointMZValues, 0.5f);
        area = 0;
        for (int i = 1; i < scanNumbers.length; i++) {
            double previousRT = dataFile.getScan(scanNumbers[i - 1]).getRetentionTime() * 60d;
            double currentRT = dataFile.getScan(scanNumbers[i]).getRetentionTime() * 60d;
            double previousHeight = dataPointIntensityValues[i - 1];
            double currentHeight = dataPointIntensityValues[i];
            area += (currentRT - previousRT) * (currentHeight + previousHeight) / 2;
        }
        fragmentScan = ScanUtils.findBestFragmentScan(dataFile, rawDataPointsRTRange, rawDataPointsMZRange);
        if (fragmentScan > 0) {
            Scan fragmentScanObject = dataFile.getScan(fragmentScan);
            int precursorCharge = fragmentScanObject.getPrecursorCharge();
            if (precursorCharge > 0) this.charge = precursorCharge;
        }
    }

    /**
     * This method returns a representative datapoint of this peak in a given
     * scan
     */
    public DataPoint getDataPoint(int scanNumber) {
        int index = Arrays.binarySearch(scanNumbers, scanNumber);
        if (index < 0) return null;
        SimpleDataPoint dp = new SimpleDataPoint(dataPointMZValues[index], dataPointIntensityValues[index]);
        return dp;
    }

    /**
     * This method returns m/z value of the chromatogram
     */
    public double getMZ() {
        return mz;
    }

    /**
     * This method returns a string with the basic information that defines this
     * peak
     * 
     * @return String information
     */
    public String toString() {
        return PeakUtils.peakToString(this);
    }

    public double getArea() {
        return area;
    }

    public double getHeight() {
        return height;
    }

    public int getMostIntenseFragmentScanNumber() {
        return fragmentScan;
    }

    public PeakStatus getPeakStatus() {
        return PeakStatus.DETECTED;
    }

    public double getRT() {
        return rt;
    }

    public Range getRawDataPointsIntensityRange() {
        return rawDataPointsIntensityRange;
    }

    public Range getRawDataPointsMZRange() {
        return rawDataPointsMZRange;
    }

    public Range getRawDataPointsRTRange() {
        return rawDataPointsRTRange;
    }

    public int getRepresentativeScanNumber() {
        return representativeScan;
    }

    public int[] getScanNumbers() {
        return scanNumbers;
    }

    public RawDataFile getDataFile() {
        return dataFile;
    }

    public IsotopePattern getIsotopePattern() {
        return isotopePattern;
    }

    public void setIsotopePattern(IsotopePattern isotopePattern) {
        this.isotopePattern = isotopePattern;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }
}
