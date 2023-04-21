package org.expasy.jpl.experimental.ms.lcmsms.filtering.filter;

import java.util.Arrays;
import junit.framework.Assert;
import org.expasy.jpl.commons.ms.peaklist.JPLIPeakList;
import org.expasy.jpl.commons.ms.peaklist.JPLPeakList;
import org.expasy.jpl.experimental.ms.peaklist.JPLExpPeakList;
import org.expasy.jpl.experimental.ms.peaklist.JPLIExpPeakList;
import org.junit.Before;
import org.junit.Test;

public class JPLSpectrumMzFilterTest {

    double[] masses = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 20, 21, 22, 23, 24, 25, 26 };

    double[] intensities = new double[] { 2, 4, 6, 8, 10, 1, 3, 5, 7, 9, 6, 8, 10, 1, 3, 5 };

    JPLIExpPeakList expPeakList;

    JPLIPeakList theoPeakList;

    JPLSpectrumMzFilter filter;

    @Before
    public void init() {
        expPeakList = new JPLExpPeakList(masses, intensities);
        theoPeakList = new JPLPeakList(masses);
    }

    @Test
    public void testProcessJPLExpPeakList1() {
        filter = JPLSpectrumMzFilter.between(4, 15);
        filter.process(expPeakList);
        double[] ms = expPeakList.getMzs();
        Assert.assertTrue(Arrays.equals(new double[] { 4, 5, 6, 7, 8, 9 }, ms));
        double[] hs = expPeakList.getIntensities();
        Assert.assertTrue(Arrays.equals(new double[] { 8, 10, 1, 3, 5, 7 }, hs));
    }

    @Test
    public void testProcessJPLExpPeakList2() {
        filter = JPLSpectrumMzFilter.greaterThan(4);
        filter.process(expPeakList);
        filter = JPLSpectrumMzFilter.lowerThan(15);
        filter.process(expPeakList);
        double[] ms = expPeakList.getMzs();
        Assert.assertTrue(Arrays.equals(new double[] { 4, 5, 6, 7, 8, 9 }, ms));
        double[] hs = expPeakList.getIntensities();
        Assert.assertTrue(Arrays.equals(new double[] { 8, 10, 1, 3, 5, 7 }, hs));
    }

    @Test
    public void testProcessJPLTheoPeakList() {
        filter = JPLSpectrumMzFilter.between(4, 15);
        filter.process(theoPeakList);
        double[] ms = theoPeakList.getMzs();
        Assert.assertTrue(Arrays.equals(new double[] { 4, 5, 6, 7, 8, 9 }, ms));
    }
}
