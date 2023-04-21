package net.openchrom.chromatogram.msd.converter.supplier.cdf.io;

import net.openchrom.chromatogram.msd.converter.supplier.cdf.TestPathHelper;
import net.openchrom.chromatogram.msd.model.core.IMassSpectrum;
import net.openchrom.chromatogram.msd.model.xic.IExtractedIonSignals;
import net.openchrom.chromatogram.msd.model.xic.ITotalIonSignals;

/**
 * IChromatogram OP17760<br/>
 * 
 * @author eselmeister
 */
public class CDFChromatogramReader_OP17760_1_ITest extends CDFChromatogramReaderTestCase {

    @Override
    protected void setUp() throws Exception {
        pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_OP17760);
        super.setUp();
    }

    public void testCDFChromatogramReader_1() {
        IMassSpectrum massSpectrum;
        ITotalIonSignals tic;
        IExtractedIonSignals xic;
        assertEquals("scanDelay", 5189, chromatogram.getScanDelay());
        assertEquals("scanInterval", 769, chromatogram.getScanInterval());
        assertEquals("operator", "Hamann", chromatogram.getOperator());
        assertEquals("file", fileImport, chromatogram.getFile());
        assertEquals("name", "OP17760", chromatogram.getName());
        assertTrue("date", chromatogram.getDate() != null);
        assertEquals("numberOfScans", 5726, chromatogram.getNumberOfScans());
        assertEquals("numberOfScanIons", 1031366, chromatogram.getNumberOfScanIons());
        assertEquals("startRetentionTime", 5189, chromatogram.getStartRetentionTime());
        assertEquals("stopRetentionTime", 4439858, chromatogram.getStopRetentionTime());
        assertEquals("minSignal", 17475.0f, chromatogram.getMinSignal());
        assertEquals("maxSignal", 9571087.0f, chromatogram.getMaxSignal());
        assertEquals("miscInfo", "439-2   142�g", chromatogram.getMiscInfo());
        tic = chromatogram.getTotalIonSignals();
        assertEquals("List<ITotalIonSignal> size", 5726, tic.size());
        assertEquals("totalIonSignal", 1024242300.0f, chromatogram.getTotalSignal());
        xic = chromatogram.getExtractedIonSignals();
        assertEquals("IExtractedIonSignals size", 5726, xic.size());
        xic = chromatogram.getExtractedIonSignals(1.0f, 600.5f);
        assertEquals("IExtractedIonSignals size", 5726, xic.size());
        massSpectrum = chromatogram.getScan(5727);
        assertTrue("massSpectrum", null == massSpectrum);
        massSpectrum = chromatogram.getScan(340);
        assertEquals("TotalSignal", 150393.0f, massSpectrum.getTotalSignal());
        massSpectrum = chromatogram.getScan(628);
        assertEquals("TotalSignal", 2747568.0f, massSpectrum.getTotalSignal());
        massSpectrum = chromatogram.getScan(5726);
        assertEquals("TotalSignal", 153220.0f, massSpectrum.getTotalSignal());
    }
}
