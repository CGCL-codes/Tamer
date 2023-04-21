package org.jcvi.trace.sanger.chromatogram.abi.tag.rate;

/**
 * @author dkatzel
 *
 *
 */
public class ScanRateUtils {

    /**
     * Get the Sampling Rate (Hz) that is displayed in the
     * Seq Analysis annotation report.
     * @param scanRate
     * @return
     */
    public static float getSamplingRateFor(ScanRate scanRate) {
        return 1000F / scanRate.getScanPeriod();
    }
}
