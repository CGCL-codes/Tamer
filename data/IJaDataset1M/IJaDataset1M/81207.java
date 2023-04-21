package net.openchrom.chromatogram.msd.model.core.support;

import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.core.IMassSpectrum;
import net.openchrom.chromatogram.msd.model.core.IPeak;
import net.openchrom.chromatogram.msd.model.core.IPeakMassSpectrum;
import net.openchrom.chromatogram.msd.model.core.IPeakModel;
import net.openchrom.chromatogram.msd.model.core.support.PeakBuilder;

/**
 * The chromatogram and peak will be initialised in DefaultPeakTestCase.<br/>
 * The peak has 15 scans, starting at a retention time of 1500 milliseconds (ms)
 * and ends at a retention time of 15500 ms.<br/>
 * The chromatogram has 17 scans, starting at a retention time of 500 ms and
 * ends at a retention time of 16500 ms. It has a background of 1750 units.
 * 
 * @author eselmeister
 */
public class PeakBuilder_13_Test extends PeakBuilderExtendedTestCase {

    private IPeak peak;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        peakMassSpectrum.adjustTotalSignal(5085.51f);
        peak = PeakBuilder.createPeak(chromatogram, totalIonSignals, peakMassSpectrum);
    }

    @Override
    protected void tearDown() throws Exception {
        peak = null;
        super.tearDown();
    }

    public void testGetBackgroundAbundanceAtScan_1() {
        float background = peak.getBackgroundAbundanceAtScan(5);
        assertEquals("background", 145.48741f, background);
    }

    public void testGetBackgroundAbundanceAtScan_2() {
        float background = peak.getBackgroundAbundanceAtScan(1);
        assertEquals("background", 0.0f, background);
    }

    public void testGetBackgroundAbundanceAtScan_3() {
        float background = peak.getBackgroundAbundanceAtScan(17);
        assertEquals("background", 0.0f, background);
    }

    public void testGetBackgroundAbundanceAtScan_4() {
        float background = peak.getBackgroundAbundanceAtScan(0);
        assertEquals("background", 0.0f, background);
    }

    public void testGetBackgroundAbundanceAtScan_5() {
        float background = peak.getBackgroundAbundanceAtScan(200);
        assertEquals("background", 0.0f, background);
    }

    public void testGetChromatogramMassSpectrum_1() {
        IMassSpectrum massSpectrum = peak.getChromatogramMassSpectrum();
        assertNotNull(massSpectrum);
        assertEquals("NumberOfIons", 11, massSpectrum.getNumberOfIons());
    }

    public void testGetExctractedMassSpectrum_1() {
        IPeakMassSpectrum massSpectrum = peak.getExtractedMassSpectrum();
        assertNotNull(massSpectrum);
        assertEquals("NumberOfIons", 8, massSpectrum.getNumberOfIons());
    }

    public void testGetModelDescription_1() {
        String modelDescription = peak.getModelDescription();
        assertEquals("ModelDescription", "", modelDescription);
    }

    public void testGetModelDescription_2() {
        String md = "TIC";
        peak.setModelDescription(md);
        String modelDescription = peak.getModelDescription();
        assertEquals("ModelDescription", md, modelDescription);
    }

    public void testGetModelDescription_3() {
        String md = "+104 -56";
        peak.setModelDescription(md);
        String modelDescription = peak.getModelDescription();
        assertEquals("ModelDescription", md, modelDescription);
    }

    public void testGetParentChromatogram_1() {
        IChromatogram chromatogram = peak.getChromatogram();
        assertNotNull("Chromatogram", chromatogram);
    }

    public void testGetPeakAbundanceAtScan_1() {
        float abundance = peak.getPeakAbundanceAtScan(1);
        assertEquals("abundance", 0.0f, abundance);
    }

    public void testGetPeakAbundanceAtScan_2() {
        float abundance = peak.getPeakAbundanceAtScan(10);
        assertEquals("abundance", 5085.5103f, abundance);
    }

    public void testGetPeakAbundanceAtScan_3() {
        float abundance = peak.getPeakAbundanceAtScan(5);
        assertEquals("abundance", 762.82654f, abundance);
    }

    public void testGetPeakAbundanceAtScan_4() {
        float abundance = peak.getPeakAbundanceAtScan(14);
        assertEquals("abundance", 1525.653f, abundance);
    }

    public void testGetPeakAbundanceAtScan_5() {
        float abundance = peak.getPeakAbundanceAtScan(17);
        assertEquals("abundance", 0.0f, abundance);
    }

    public void testGetPeakModel_1() {
        IPeakModel peakModel = peak.getPeakModel();
        assertNotNull("PeakModel", peakModel);
        assertEquals("StartRetentionTime", 1500, peakModel.getStartRetentionTime());
        assertEquals("StopRetentionTime", 15500, peakModel.getStopRetentionTime());
    }

    public void testGetScanMax_1() {
        int scanMax = peak.getScanMax();
        assertEquals("ScanMax", 10, scanMax);
    }

    public void testGetWidthBaselineTotalInScans_1() {
        int width = peak.getWidthBaselineTotalInScans();
        assertEquals("Width", 15, width);
    }

    public void testGetIntegratorDescription_1() {
        assertEquals("IntegratorDescription", "", peak.getIntegratorDescription());
    }

    public void testGetIntegratorDescription_2() {
        String description = "Default Integrator";
        peak.setIntegratorDescription(description);
        assertEquals("IntegratorDescription", description, peak.getIntegratorDescription());
        peak.setIntegratorDescription("");
        assertEquals("IntegratorDescription", "", peak.getIntegratorDescription());
    }

    public void testGetIntegratorDescription_3() {
        peak.setIntegratorDescription(null);
        assertEquals("IntegratorDescription", "", peak.getIntegratorDescription());
    }

    public void testGetDetectorDescription_1() {
        assertEquals("DetectorDescription", "", peak.getDetectorDescription());
    }

    public void testGetDetectorDescription_2() {
        String description = "Default Detector";
        peak.setDetectorDescription(description);
        assertEquals("DetectorDescription", description, peak.getDetectorDescription());
        peak.setDetectorDescription("");
        assertEquals("DetectorDescription", "", peak.getDetectorDescription());
    }

    public void testGetDetectorDescription_3() {
        peak.setDetectorDescription(null);
        assertEquals("DetectorDescription", "", peak.getDetectorDescription());
    }
}
