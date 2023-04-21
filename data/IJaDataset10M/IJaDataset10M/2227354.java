package org.expasy.jpl.core.mol.polymer.pept.fragmenter;

import org.expasy.jpl.core.mol.monomer.aa.AminoAcid;
import org.expasy.jpl.core.mol.polymer.pept.PeptideTypeImpl;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotationImpl;
import org.expasy.jpl.core.ms.spectrum.peak.AnnotatedPeak;
import org.junit.Before;
import org.junit.Test;

public class JPLTemporaryPeaksManagerTest {

    TemporaryPeaksManager pool = TemporaryPeaksManager.newInstance();

    @Before
    public void setup() {
        pool.resetPool();
        pool.addPeak(new AnnotatedPeak(22, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.A, 3).charge(2).build()));
        pool.addPeak(new AnnotatedPeak(45, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.Y, 3).charge(2).build()));
        pool.addPeak(new AnnotatedPeak(67, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.A, 2).charge(2).build()));
        pool.addPeak(new AnnotatedPeak(76, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.X, 3).charge(2).build()));
        pool.addPeak(new AnnotatedPeak(105, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.C, 3).charge(2).build()));
        pool.addPeak(new AnnotatedPeak(13, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.I, 1).aa(AminoAcid.RAD_D).build()));
        pool.addPeak(new AnnotatedPeak(4, new FragmentAnnotationImpl.Builder(PeptideTypeImpl.I, 1).aa(AminoAcid.IMMO_E).build()));
    }

    @Test
    public void testPoolDisplay() {
        System.out.println("allocated objects = " + pool.getAllocatedObject());
        System.out.println("living objects = " + pool.getActivePeakNumber());
        for (int i = 0; i < pool.getActivePeakNumber(); i++) {
            System.out.println("peak " + i + ": " + pool.getPeakAt(i));
        }
        System.out.println(pool.getPeakList(null));
    }
}
