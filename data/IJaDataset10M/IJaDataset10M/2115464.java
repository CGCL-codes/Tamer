package net.openchrom.chromatogram.msd.filter.supplier.denoising.settings;

import junit.framework.TestCase;

public class DenoisingFilterSettings_1_Test extends TestCase {

    private IDenoisingFilterSettings settings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        settings = new DenoisingFilterSettings();
    }

    @Override
    protected void tearDown() throws Exception {
        settings = null;
        super.tearDown();
    }

    public void testGetIonsToRemove_1() {
        assertNotNull(settings.getIonsToRemove());
    }

    public void testGetIonsToRemove_2() {
        assertEquals("IonsToRemove Size", 0, settings.getIonsToRemove().sizeNominal());
    }

    public void testGetIonsToPreserve_1() {
        assertNotNull(settings.getIonsToPreserve());
    }

    public void testGetIonsToPreserve_2() {
        assertEquals("IonsToPreserve Size", 0, settings.getIonsToPreserve().sizeNominal());
    }

    public void testGetAdjustThresholdTransitions_1() {
        assertTrue(settings.getAdjustThresholdTransitions());
    }

    public void testGetAdjustThresholdTransitions_2() {
        settings.setAdjustThresholdTransitions(false);
        assertFalse(settings.getAdjustThresholdTransitions());
    }

    public void testGetNumberOfUsedIonsForCoefficient_1() {
        assertEquals("NumberOfUsedIonsForCoefficient", 1, settings.getNumberOfUsedIonsForCoefficient());
    }

    public void testGetNumberOfUsedIonsForCoefficient_2() {
        settings.setNumberOfUsedIonsForCoefficient(5);
        assertEquals("NumberOfUsedIonsForCoefficient", 5, settings.getNumberOfUsedIonsForCoefficient());
    }

    public void testGetNumberOfUsedIonsForCoefficient_3() {
        settings.setNumberOfUsedIonsForCoefficient(0);
        assertEquals("NumberOfUsedIonsForCoefficient", 1, settings.getNumberOfUsedIonsForCoefficient());
    }

    public void testGetNumberOfUsedIonsForCoefficient_4() {
        settings.setNumberOfUsedIonsForCoefficient(-1);
        assertEquals("NumberOfUsedIonsForCoefficient", 1, settings.getNumberOfUsedIonsForCoefficient());
    }
}
