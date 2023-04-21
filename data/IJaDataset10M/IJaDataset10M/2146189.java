package net.openchrom.chromatogram.msd.model.implementation;

import junit.framework.TestCase;

/**
 * Equals, hashCode
 * 
 * @author eselmeister
 */
public class DefaultMassSpectrum_2_Test extends TestCase {

    private DefaultMassSpectrum massSpectrum1;

    private DefaultMassSpectrum massSpectrum2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        massSpectrum1 = new DefaultMassSpectrum();
        massSpectrum1.addIon(new DefaultIon(45.4f, 7830.4f));
        massSpectrum2 = new DefaultMassSpectrum();
    }

    @Override
    protected void tearDown() throws Exception {
        massSpectrum1 = null;
        massSpectrum2 = null;
        super.tearDown();
    }

    public void testEquals_1() {
        assertEquals("equals", false, massSpectrum1.equals(massSpectrum2));
    }

    public void testEquals_2() {
        assertEquals("equals", false, massSpectrum2.equals(massSpectrum1));
    }

    public void testHashCode_1() {
        assertTrue("hashCode", massSpectrum1.hashCode() != massSpectrum2.hashCode());
    }

    public void testHashCode_2() {
        assertTrue("hashCode", massSpectrum2.hashCode() != massSpectrum1.hashCode());
    }
}
