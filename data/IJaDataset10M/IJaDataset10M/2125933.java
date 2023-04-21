package org.expasy.jpl.core.ms.spectrum;

import junit.framework.Assert;
import org.expasy.jpl.core.mol.polymer.pept.PeptideTypeImpl;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotationImpl;
import org.expasy.jpl.core.ms.spectrum.peak.InvalidPeakException;
import org.junit.Before;
import org.junit.Test;

public class JPLMSPeakListManagerTest {

    double[] masses = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    double[] intensities = new double[] { 2, 4, 6, 8, 10, 1, 3, 5, 7, 9 };

    PeakListImpl pl;

    PeakListSetter manager;

    @Before
    public void setUp() throws Exception {
        manager = PeakListSetter.getInstance();
        pl = new PeakListImpl.Builder(masses).intensities(intensities).build();
    }

    @Test
    public void testSetMz() throws InvalidPeakException {
        manager.setMzAt(pl, 2.9, 1);
        Assert.assertEquals(pl.getMzAt(1), 2.9, 0.1);
    }

    @Test(expected = InvalidPeakException.class)
    public void testSetBadMz() throws InvalidPeakException {
        manager.setMzAt(pl, 3.9, 1);
    }

    @Test
    public void testSetIntensity() throws InvalidPeakException {
        manager.setIntensityAt(pl, 29, 1);
        Assert.assertEquals(pl.getIntensityAt(1), 29, 0.1);
    }

    @Test
    public void testSetIntensity2() throws InvalidPeakException {
        new PeakListImpl.EditorImpl(pl).resetIntensityAt(29, 1).edit();
        Assert.assertEquals(29, pl.getIntensityAt(1), 0.1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetBadIndex() throws InvalidPeakException {
        manager.setIntensityAt(pl, 29, 19);
    }

    @Test
    public void testAddAndRemoveAnnot() {
        manager.addAnnotationAt(pl, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.A, 2).build(), 1);
        Assert.assertEquals("a2", pl.getAnnotationsAt(1).get(0).toString());
        manager.removeAnnotationsAt(pl, 1);
        Assert.assertFalse(pl.hasAnnotations());
    }
}
