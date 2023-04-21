package net.sf.mzmine.data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.sf.mzmine.util.Range;

/**
 * This interface defines the properties of a detected peak
 */
public interface ChromatographicPeak {

    /**
     * This method returns the status of the peak
     */
    @Nonnull
    public PeakStatus getPeakStatus();

    /**
     * This method returns raw M/Z value of the peak
     */
    public double getMZ();

    /**
     * This method returns raw retention time of the peak
     */
    public double getRT();

    /**
     * This method returns the raw height of the peak
     */
    public double getHeight();

    /**
     * This method returns the raw area of the peak
     */
    public double getArea();

    /**
     * Returns raw data file where this peak is present
     */
    @Nonnull
    public RawDataFile getDataFile();

    /**
     * This method returns numbers of scans that contain this peak
     */
    @Nonnull
    public int[] getScanNumbers();

    /**
     * This method returns number of most representative scan of this peak
     */
    public int getRepresentativeScanNumber();

    /**
     * This method returns m/z and intensity of this peak in a given scan. This
     * m/z and intensity does not need to match any actual raw data point. May
     * return null, if there is no data point in given scan.
     */
    @Nullable
    public DataPoint getDataPoint(int scanNumber);

    /**
     * Returns the retention time range of all raw data points used to detect
     * this peak
     */
    @Nonnull
    public Range getRawDataPointsRTRange();

    /**
     * Returns the range of m/z values of all raw data points used to detect
     * this peak
     */
    @Nonnull
    public Range getRawDataPointsMZRange();

    /**
     * Returns the range of intensity values of all raw data points used to
     * detect this peak
     */
    @Nonnull
    public Range getRawDataPointsIntensityRange();

    /**
     * Returns the number of scan that represents the fragmentation of this peak
     * in MS2 level.
     */
    @Nonnull
    public int getMostIntenseFragmentScanNumber();

    /**
     * Returns the isotope pattern of this peak or null if no pattern is
     * attached
     */
    @Nullable
    public IsotopePattern getIsotopePattern();

    /**
     * Sets the isotope pattern of this peak
     */
    public void setIsotopePattern(@Nonnull IsotopePattern isotopePattern);

    /**
     * Returns the charge of this ion. If the charge is unknown, returns 0.
     */
    public int getCharge();

    /**
     * Sets the charge of this ion
     */
    public void setCharge(int charge);
}
