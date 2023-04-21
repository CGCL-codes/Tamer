package org.expasy.jpl.tools.qm.dataloader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.io.IOException;
import org.expasy.jpl.core.ms.spectrum.peak.InvalidPeakException;
import org.expasy.jpl.io.ms.MSScan;
import org.expasy.jpl.tools.qm.misc.QMPrecursor;
import org.junit.Before;
import org.junit.Test;

public class TestSinglePeakListLoader {

    private static String hmsMgfFileName;

    private static String hmsSptxtFileName;

    private static SinglePeakListLoader spllMgf;

    private static SinglePeakListLoader spllSptxt;

    @Before
    public void setUp() throws Exception {
        hmsMgfFileName = ClassLoader.getSystemResource("data/testMgf.hms").getFile();
        spllMgf = new SinglePeakListLoader(hmsMgfFileName);
        hmsSptxtFileName = ClassLoader.getSystemResource("data/testSptxt.hms").getFile();
        spllSptxt = new SinglePeakListLoader(hmsSptxtFileName);
    }

    @Test
    public void testLoadPeakListMgf() throws java.text.ParseException, IOException, InvalidPeakException {
        assertEquals(592.854858, spllMgf.loadScan(1).getPeakList().getPrecursor().getMz(), 0.01);
        assertEquals(590.8123715, spllMgf.loadScan(2).getPeakList().getPrecursor().getMz(), 0.01);
        assertEquals(588.831299, spllMgf.loadScan(3).getPeakList().getPrecursor().getMz(), 0.01);
    }

    @Test
    public void testLoadPeakListSptxt() throws java.text.ParseException, IOException, InvalidPeakException {
        MSScan scan = spllSptxt.loadScan(1);
        QMPrecursor prec = (QMPrecursor) scan.getPeakList().getPrecursor();
        System.out.println(scan.getPeakList().getPrecursor().getPeptide());
        assertEquals("H_IYQY({79.97})IQSR_HO", prec.getPeptide().toString());
        assertFalse(prec.isDecoy());
    }
}
