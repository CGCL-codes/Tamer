package net.openchrom.chromatogram.msd.converter.supplier.cdf.io;

import net.openchrom.chromatogram.msd.converter.supplier.cdf.TestPathHelper;

/**
 * Will the memory be cleaned up, or does memory leaks appear while import the
 * chromatograms?
 * 
 * @author eselmeister
 */
public class CDFChromatogramReader_MemoryTest_1_ITest extends CDFChromatogramReaderTestCase {

    @Override
    protected void setUp() throws Exception {
        pathImport = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_OP17760);
        super.setUp();
    }

    public void testRead_1() {
        assertTrue("1", chromatogram != null);
    }

    public void testRead_2() {
        assertTrue("2", chromatogram != null);
    }

    public void testRead_3() {
        assertTrue("3", chromatogram != null);
    }

    public void testRead_4() {
        assertTrue("4", chromatogram != null);
    }

    public void testRead_5() {
        assertTrue("5", chromatogram != null);
    }

    public void testRead_6() {
        assertTrue("6", chromatogram != null);
    }

    public void testRead_7() {
        assertTrue("7", chromatogram != null);
    }

    public void testRead_8() {
        assertTrue("8", chromatogram != null);
    }

    public void testRead_9() {
        assertTrue("9", chromatogram != null);
    }

    public void testRead_10() {
        assertTrue("10", chromatogram != null);
    }

    public void testRead_11() {
        assertTrue("11", chromatogram != null);
    }

    public void testRead_12() {
        assertTrue("12", chromatogram != null);
    }

    public void testRead_13() {
        assertTrue("13", chromatogram != null);
    }

    public void testRead_14() {
        assertTrue("14", chromatogram != null);
    }

    public void testRead_15() {
        assertTrue("15", chromatogram != null);
    }

    public void testRead_16() {
        assertTrue("16", chromatogram != null);
    }

    public void testRead_17() {
        assertTrue("17", chromatogram != null);
    }

    public void testRead_18() {
        assertTrue("18", chromatogram != null);
    }

    public void testRead_19() {
        assertTrue("19", chromatogram != null);
    }

    public void testRead_20() {
        assertTrue("20", chromatogram != null);
    }
}
