package net.openchrom.chromatogram.msd.filter.supplier.denoising.internal.core.support;

import junit.framework.TestCase;

public class IonNoise_1_Test extends TestCase {

    private IonNoise ionNoise;

    @Override
    protected void setUp() throws Exception {
        ionNoise = new IonNoise(167, 5893.56f);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetIon_1() {
        assertEquals("Ion", 167, ionNoise.getIon());
    }

    public void testGetAbundance_1() {
        assertEquals("Abundance", 5893.56f, ionNoise.getAbundance());
    }
}
