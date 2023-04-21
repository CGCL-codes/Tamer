package org.expasy.jpl.experimental.ms.lcmsms.filtering.filter;

import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.commons.ms.filtering.filter.JPLSpectrumFilter;
import org.expasy.jpl.commons.ms.peaklist.JPLIPeakList;
import org.expasy.jpl.experimental.ms.peaklist.JPLIExpPeakList;

/**
 * Class to select all peak clusters (by mz value) and replace their mass/intensity values by 
 * the centroid values calculated within the cluster. 
 * 
 * @author mueller
 *
 */
public class JPLPeakGrouper extends JPLSpectrumFilter {

    Log logger = LogFactory.getLog(JPLPeakGrouper.class);

    /** Maximum distance between consecutive mz values in same peak group */
    private double mzMaxDiff;

    /** Table to keep track of how many times a peak is present in all spectra*/
    private int[] nrOfOccurences;

    /** Helper class for the calculation of groups */
    protected class JPLInterval {

        public JPLInterval(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /** index of valley before peak */
        public int min;

        /** index of valley after peak */
        public int max;
    }

    /**
	 * Default constructor
	 */
    public JPLPeakGrouper() {
        this.setMzMaxDiff(0.3);
        nrOfOccurences = null;
    }

    /**
	 * Constructs JPLPeakGrouper object with mzMaxDiff calculated from inputSpectrum.
	 * This object can reused to process other spectra with same mzMaxDiff.
	 * @param inputSpectrum: Spectrum, which is used to estimate mzMaxDiff
	 * @return JPLPeakGrouper Object
	 */
    public static JPLPeakGrouper adaptedPeakGrouper(JPLIExpPeakList inputSpectrum) {
        JPLPeakGrouper filter = new JPLPeakGrouper();
        filter.calcMzMaxDiff(inputSpectrum);
        return filter;
    }

    /**
	 * Calculates maximal distance within peak groups. 
	 * @param inputSpectrum: Spectrum to determine maximal distance within peak groups.
	 */
    public void calcMzMaxDiff(JPLIExpPeakList inputSpectrum) {
    }

    /**
	 * Find the peak clusters. It is assumed that there is a reasonable separation
	 * between these clusters.
	 * @param inputSpectrum: Experimental spectrum
	 * @return List of peak groups 
	 */
    private Vector<JPLInterval> getGroups(JPLIExpPeakList inputSpectrum) {
        Vector<JPLInterval> maxima = new Vector<JPLInterval>();
        double[] masses = inputSpectrum.getMzs();
        int iMin = 0;
        int iMax = 0;
        boolean newGroup = false;
        for (int i = 0; i < masses.length; i++) {
            if (i > 0 && masses[i] - masses[i - 1] > mzMaxDiff) {
                newGroup = true;
                iMax = i - 1;
            }
            if (newGroup) {
                maxima.add(new JPLInterval(iMin, iMax));
                newGroup = false;
                iMin = i;
            }
        }
        maxima.add(new JPLInterval(iMin, masses.length - 1));
        return (maxima);
    }

    /**
	 * Selects peak groups and calculates their centroid values. Returns the same but processed spectrum object
	 * Throws runtime exception IllegalArgumentException if lengths of mass and occurrence arrays differ.
	 * 
	 * @param spectrum: The spectrum to process
	 * @return The processed spectrum
	 */
    private void filter(JPLIExpPeakList spectrum) {
        Vector<JPLInterval> maxima = getGroups(spectrum);
        double[] cHeights = new double[maxima.size()];
        double[] cMasses = new double[maxima.size()];
        int[] cOcc = new int[maxima.size()];
        double[] heights = spectrum.getIntensities();
        double[] masses = spectrum.getMzs();
        if (nrOfOccurences != null && nrOfOccurences.length != masses.length) {
            throw new IllegalArgumentException("Incompatible length of mass and occurrence array");
        }
        for (int i = 0; i < maxima.size(); i++) {
            JPLInterval interval = maxima.get(i);
            double h = 0.0;
            double m = 0.0;
            double mTot = 0.0;
            int cnt = 0;
            for (int j = interval.min; j <= interval.max; j++) {
                h += heights[j];
                m += heights[j] * masses[j];
                mTot += masses[j];
                if (nrOfOccurences == null) {
                    cnt = interval.max - interval.min + 1;
                } else {
                    cnt += nrOfOccurences[j];
                }
            }
            if (h > 0) {
                cMasses[i] = m / h;
                cHeights[i] = h;
            } else {
                cMasses[i] = mTot / cnt;
                cHeights[i] = 0.0;
            }
            cOcc[i] = cnt;
        }
        spectrum.setMzs(cMasses, false);
        spectrum.setIntensities(cHeights);
        nrOfOccurences = cOcc;
    }

    @Override
    public <T extends JPLIPeakList> void process(T spectrum) {
        if (spectrum instanceof JPLIExpPeakList) {
            filter((JPLIExpPeakList) spectrum);
        } else {
            throw new UnsupportedOperationException("process is not supported" + " for " + spectrum.getClass().getSimpleName());
        }
    }

    /**
	 * Get maximum allowed distance between consecutive mz values in same peak group
	 * @return Maximum distance
	 */
    public double getMzMaxDiff() {
        return mzMaxDiff;
    }

    /**
	 * Set maximum allowed distance between consecutive mz values in same peak group
	 * @param mzMaxDiff: Maximum distance
	 */
    public void setMzMaxDiff(double mzMaxDiff) {
        this.mzMaxDiff = mzMaxDiff;
    }

    /**
	 * Set array with the number of spectra in which each peak occurred
	 * @return Number of occurrences for each peak
	 */
    public void setNrOfOccurences(int[] nrOfOccurences) {
        this.nrOfOccurences = nrOfOccurences;
    }

    /**
	 * Get array with the number of spectra in which each peak occurred
	 * @return Number of occurrences for each peak
	 */
    public int[] getNrOfOccurences() {
        return nrOfOccurences;
    }
}
