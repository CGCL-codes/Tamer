package net.openchrom.chromatogram.msd.model.implementation;

import net.openchrom.chromatogram.msd.model.core.IPeak;
import net.openchrom.chromatogram.msd.model.exceptions.PeakException;

/**
 * The chromatogram and peak will be initialized in DefaultPeakTestCase.<br/>
 * The peak has 15 scans, starting at a retention time of 1500 milliseconds (ms)
 * and ends at a retention time of 15500 ms.<br/>
 * The chromatogram has 17 scans, starting at a retention time of 500 ms and
 * ends at a retention time of 16500 ms. It has a background of 1750 units.
 * 
 * @author eselmeister
 */
public class DefaultPeak_2_Test extends DefaultPeakTestCase {

    private IPeak peak;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        peak = null;
        super.tearDown();
    }

    public void testConstructor_1() {
        try {
            peak = new DefaultPeak(getPeakModel(), getChromatogram());
        } catch (IllegalArgumentException e) {
            assertFalse("A IllegalArgumentException should not be thrown here.", true);
        } catch (PeakException e) {
            assertFalse("A PeakException should not be thrown here.", true);
        }
        assertNotNull(peak);
    }

    public void testConstructor_2() {
        try {
            peak = new DefaultPeak(null, getChromatogram());
        } catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        } catch (PeakException e) {
            assertFalse("A PeakException should not be thrown here.", true);
        }
        assertNull(peak);
    }

    public void testConstructor_3() {
        try {
            peak = new DefaultPeak(getPeakModel(), null);
        } catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        } catch (PeakException e) {
            assertFalse("A PeakException should not be thrown here.", true);
        }
        assertNull(peak);
    }

    public void testConstructor_4() {
        try {
            peak = new DefaultPeak(null, null);
        } catch (IllegalArgumentException e) {
            assertTrue("IllegalArgumentException", true);
        } catch (PeakException e) {
            assertFalse("A PeakException should not be thrown here.", true);
        }
        assertNull(peak);
    }
}
