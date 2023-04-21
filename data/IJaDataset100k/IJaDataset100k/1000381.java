package org.expasy.jpl.core.ms.spectrum.editor;

import java.util.Arrays;
import junit.framework.Assert;
import org.expasy.jpl.core.ms.spectrum.PeakList;
import org.expasy.jpl.core.ms.spectrum.PeakListImpl;
import org.junit.Before;
import org.junit.Test;

public class JPLPeakGrouperTest {

    double[] masses = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 21, 22, 23, 24, 25, 26, 40, 41, 50 };

    double[] intensities = new double[] { 2, 4, 6, 8, 10, 1, 3, 5, 7, 9, 6, 8, 10, 1, 3, 5, 2, 3, 10 };

    int[] occurences = new int[] { 9, 7, 2, 1 };

    PeakList expPeakList;

    PeakGrouper filter;

    @Before
    public void init() {
        expPeakList = new PeakListImpl.Builder(masses).intensities(intensities).build();
        filter = new PeakGrouper();
    }

    @Test
    public void testCalcMzSamplingDist() {
        filter.calcMzMaxDiff(expPeakList);
        final double mzDiff = filter.getMzMaxDiff();
        Assert.assertTrue(0.3 == mzDiff);
    }

    @Test
    public void testProcessJPLExpPeakList() {
        filter = PeakGrouper.adaptedPeakGrouper(expPeakList);
        filter.setMzMaxDiff(5);
        expPeakList = filter.transform(expPeakList);
        final double h1 = 2 + 4 + 6 + 8 + 10 + 1 + 3 + 5 + 7;
        final double m1 = (1 * 2 + 2 * 4 + 3 * 6 + 4 * 8 + 5 * 10 + 6 * 1 + 7 * 3 + 8 * 5 + 9 * 7) / h1;
        final double h2 = 9 + 6 + 8 + 10 + 1 + 3 + 5;
        final double m2 = (20 * 9 + 21 * 6 + 22 * 8 + 23 * 10 + 24 * 1 + 25 * 3 + 26 * 5) / h2;
        final double h3 = 2 + 3;
        final double m3 = (40 * 2 + 41 * 3) / h3;
        final double h4 = 10;
        final double m4 = (50 * 10) / h4;
        final double[] ms = expPeakList.getMzs(null);
        final double[] hs = expPeakList.getIntensities(null);
        Assert.assertTrue(Arrays.equals(new double[] { m1, m2, m3, m4 }, ms));
        Assert.assertTrue(Arrays.equals(new double[] { h1, h2, h3, h4 }, hs));
        Assert.assertTrue(Arrays.equals(new int[] { 9, 7, 2, 1 }, filter.getNrOfOccurences()));
    }
}
