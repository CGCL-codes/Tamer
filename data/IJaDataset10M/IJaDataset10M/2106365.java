package net.openchrom.chromatogram.msd.converter.supplier.amdis.model;

import junit.framework.TestCase;

public class AmdisMassSpectrum_1_Test extends TestCase {

    private IAmdisMassSpectrum massSpectrum;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        massSpectrum = new AmdisMassSpectrum();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetSource_1() {
        assertEquals("Source", "", massSpectrum.getSource());
    }

    public void testGetSource_2() {
        massSpectrum.setSource("file something");
        assertEquals("Source", "file something", massSpectrum.getSource());
    }

    public void testGetSource_3() {
        massSpectrum.setSource(null);
        assertEquals("Source", "", massSpectrum.getSource());
    }
}
