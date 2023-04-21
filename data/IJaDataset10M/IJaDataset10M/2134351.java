package net.openchrom.chromatogram.msd.model.implementation;

import net.openchrom.chromatogram.msd.model.core.IIon;
import net.openchrom.chromatogram.msd.model.core.IIonBounds;
import net.openchrom.chromatogram.msd.model.exceptions.AbundanceLimitExceededException;
import net.openchrom.chromatogram.msd.model.exceptions.IonLimitExceededException;
import junit.framework.TestCase;

/**
 * chromatogram = new DefaultChromatogram();
 * massSpectrum.setParentChromatogram(chromatogram); ion = new
 * DefaultIon(45.5f, 3000.5f);
 * massSpectrum.addIon(ion);
 * 
 * @author eselmeister
 */
public class DefaultMassSpectrum_4_Test extends TestCase {

    private DefaultChromatogram chromatogram;

    private DefaultMassSpectrum massSpectrum;

    private DefaultIon ion;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        chromatogram = new DefaultChromatogram();
        massSpectrum = new DefaultMassSpectrum();
        massSpectrum.setParentChromatogram(chromatogram);
        ion = new DefaultIon(45.5f, 3000.5f);
        massSpectrum.addIon(ion);
    }

    @Override
    protected void tearDown() throws Exception {
        massSpectrum = null;
        ion = null;
        super.tearDown();
    }

    public void testGetIons_1() {
        assertEquals("getIons", 1, massSpectrum.getIons().size());
    }

    public void testGetParentChromatogram_1() {
        assertEquals("getParentChromatogram", chromatogram, massSpectrum.getParentChromatogram());
    }

    public void getTotalSignal_1() {
        assertEquals("getTotalSignal", 3000.5f, massSpectrum.getTotalSignal());
    }

    public void getExtractedIonSignal_1() {
        assertEquals("getExtractedIonSignal", 0, massSpectrum.getExtractedIonSignal().getAbundance(0));
    }

    public void getExtractedIonSignal_2() {
        assertEquals("getExtractedIonSignal", 3000.5f, massSpectrum.getExtractedIonSignal(1, 50).getAbundance(46));
    }

    public void testGetBasePeak_1() {
        assertEquals("getBasePeak", 45.5f, massSpectrum.getBasePeak());
    }

    public void testGetBasePeakAbundance_1() {
        assertEquals("getBasePeakAbundance", 3000.5f, massSpectrum.getBasePeakAbundance());
    }

    public void testGetHighestAbundance_1() {
        assertEquals("getHighestAbundance", 3000.5f, massSpectrum.getHighestAbundance().getAbundance());
    }

    public void testGetHighestAbundance_2() {
        assertEquals("getHighestAbundance", 45.5f, massSpectrum.getHighestAbundance().getIon());
    }

    public void testGetHighestIon_1() {
        assertEquals("getHighestIon", 3000.5f, massSpectrum.getHighestIon().getAbundance());
    }

    public void testGetHighestIon_2() {
        assertEquals("getHighestIon", 45.5f, massSpectrum.getHighestIon().getIon());
    }

    public void testGetLowestAbundance_1() {
        assertEquals("getLowestAbundance", 3000.5f, massSpectrum.getLowestAbundance().getAbundance());
    }

    public void testGetLowestAbundance_2() {
        assertEquals("getLowestAbundance", 45.5f, massSpectrum.getLowestAbundance().getIon());
    }

    public void testGetLowestIon_1() {
        assertEquals("getLowestIon", 3000.5f, massSpectrum.getLowestIon().getAbundance());
    }

    public void testGetLowestIon_2() {
        assertEquals("getLowestIon", 45.5f, massSpectrum.getLowestIon().getIon());
    }

    public void testGetIonBounds_1() {
        IIonBounds bounds = massSpectrum.getIonBounds();
        assertEquals("getIonBounds", 3000.5f, bounds.getLowestIon().getAbundance());
        assertEquals("getIonBounds", 45.5f, bounds.getLowestIon().getIon());
        assertEquals("getIonBounds", 3000.5f, bounds.getHighestIon().getAbundance());
        assertEquals("getIonBounds", 45.5f, bounds.getHighestIon().getIon());
    }

    public void testGetNumberOfIons_1() {
        assertEquals("getNumberOfIons", 1, massSpectrum.getNumberOfIons());
    }

    public void testGetIon_1() {
        IIon ion;
        try {
            ion = massSpectrum.getIon(5);
            assertEquals("getIon", null, ion);
            ion = massSpectrum.getIon(46);
            assertTrue("getIon", ion != null);
            assertEquals("getIon(46) abundance", 3000.5f, ion.getAbundance());
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
