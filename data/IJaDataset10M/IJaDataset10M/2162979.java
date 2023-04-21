package net.openchrom.chromatogram.msd.model.implementation;

import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.core.IIonBounds;
import net.openchrom.chromatogram.msd.model.exceptions.AbundanceLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.IonLimitExceededException;
import junit.framework.TestCase;

/**
 * + ion = new DefaultIon(45.5f, 78500.2f); + ion =
 * new DefaultIon(85.4f, 3000.5f); + ion = new
 * DefaultIon(85.4f, 3000.5f); // insert it twice! + ion = new
 * DefaultIon(104.1f, 120000.4f);
 * 
 * @author eselmeister
 */
public class DefaultMassSpectrum_6_Test extends TestCase {

    private DefaultMassSpectrum massSpectrum;

    private DefaultIon ion;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        massSpectrum = new DefaultMassSpectrum();
        ion = new DefaultIon(45.5f, 78500.2f);
        massSpectrum.addIon(ion);
        ion = new DefaultIon(85.4f, 3000.5f);
        massSpectrum.addIon(ion);
        ion = new DefaultIon(85.4f, 3000.5f);
        massSpectrum.addIon(ion);
        ion = new DefaultIon(104.1f, 120000.4f);
        massSpectrum.addIon(ion);
    }

    @Override
    protected void tearDown() throws Exception {
        massSpectrum = null;
        ion = null;
        super.tearDown();
    }

    public void testGetIons_1() {
        assertEquals("getIons", 3, massSpectrum.getIons().size());
    }

    public void getTotalSignal_1() {
        assertEquals("getTotalSignal", 201501.1f, massSpectrum.getTotalSignal());
    }

    public void getExtractedIonSignal_1() {
        assertEquals("getExtractedIonSignal", 0, massSpectrum.getExtractedIonSignal().getAbundance(0));
    }

    public void getExtractedIonSignal_2() {
        assertEquals("getExtractedIonSignal", 120000.4f, massSpectrum.getExtractedIonSignal(40, 120).getAbundance(104));
    }

    public void getExtractedIonSignal_3() {
        assertEquals("getExtractedIonSignal", 3000.5f, massSpectrum.getExtractedIonSignal(40, 120).getAbundance(85));
    }

    public void getExtractedIonSignal_4() {
        assertEquals("getExtractedIonSignal", 78500.2f, massSpectrum.getExtractedIonSignal(40, 120).getAbundance(46));
    }

    public void testGetBasePeak_1() {
        assertEquals("getBasePeak", 104.1f, massSpectrum.getBasePeak());
    }

    public void testGetBasePeakAbundance_1() {
        assertEquals("getBasePeakAbundance", 120000.4f, massSpectrum.getBasePeakAbundance());
    }

    public void testGetHighestAbundance_1() {
        assertEquals("getHighestAbundance", 120000.4f, massSpectrum.getHighestAbundance().getAbundance());
    }

    public void testGetHighestAbundance_2() {
        assertEquals("getHighestAbundance", 104.1f, massSpectrum.getHighestAbundance().getIon());
    }

    public void testGetHighestIon_1() {
        assertEquals("getHighestIon", 120000.4f, massSpectrum.getHighestIon().getAbundance());
    }

    public void testGetHighestIon_2() {
        assertEquals("getHighestIon", 104.1f, massSpectrum.getHighestIon().getIon());
    }

    public void testGetLowestAbundance_1() {
        assertEquals("getLowestAbundance", 3000.5f, massSpectrum.getLowestAbundance().getAbundance());
    }

    public void testGetLowestAbundance_2() {
        assertEquals("getLowestAbundance", 85.4f, massSpectrum.getLowestAbundance().getIon());
    }

    public void testGetLowestIon_1() {
        assertEquals("getLowestIon", 78500.2f, massSpectrum.getLowestIon().getAbundance());
    }

    public void testGetLowestIon_2() {
        assertEquals("getLowestIon", 45.5f, massSpectrum.getLowestIon().getIon());
    }

    public void testGetIonBounds_1() {
        IIonBounds bounds = massSpectrum.getIonBounds();
        assertEquals("getLowestIon().getAbundance()", 78500.2f, bounds.getLowestIon().getAbundance());
        assertEquals("getLowestIon().getIon()", 45.5f, bounds.getLowestIon().getIon());
        assertEquals("getHighestIon().getAbundance()", 120000.4f, bounds.getHighestIon().getAbundance());
        assertEquals("getHighestIon().getIon()", 104.1f, bounds.getHighestIon().getIon());
    }

    public void testGetNumberOfIons_1() {
        assertEquals("getNumberOfIons", 3, massSpectrum.getNumberOfIons());
    }

    public void testGetIon_1() {
        IIon ion;
        try {
            ion = massSpectrum.getIon(5);
            assertEquals("getIon", null, ion);
            ion = massSpectrum.getIon(46);
            assertTrue("getIon", ion != null);
            assertEquals("getIon(46) abundance", 78500.2f, ion.getAbundance());
            assertEquals("getIon(46) ion", 46.0f, ion.getIon());
        } catch (AbundanceLimitExceededException e) {
            assertTrue("AbundanceLimitExceededException", false);
        } catch (IonLimitExceededException e) {
            assertTrue("IonLimitExceededException", false);
        }
    }

    public void testIsDirty_1() {
        assertEquals("isDirty", true, massSpectrum.isDirty());
    }
}
