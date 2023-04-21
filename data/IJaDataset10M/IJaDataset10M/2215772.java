package net.openchrom.chromatogram.msd.model.implementation;

import net.openchrom.chromatogram.msd.model.core.IPeakMassSpectrum;
import junit.framework.TestCase;

public class DefaultPeakMassSpectrum_2_Test extends TestCase {

    @SuppressWarnings("unused")
    private IPeakMassSpectrum peakMassSpectrum;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        peakMassSpectrum = null;
        super.tearDown();
    }

    public void testGetNumberOfIons_1() {
        try {
            peakMassSpectrum = new DefaultPeakMassSpectrum(null);
        } catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        }
    }
}
