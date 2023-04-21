package net.openchrom.chromatogram.msd.model.xic;

import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.implementation.DefaultChromatogram;
import junit.framework.TestCase;

public class TotalIonSignals_3_Test extends TestCase {

    private ITotalIonSignals signals;

    private IChromatogram chromatogram;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        chromatogram = new DefaultChromatogram();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstruct_1() {
        signals = new TotalIonSignals(10, chromatogram);
        assertNotNull("getChromatogram", signals.getChromatogram());
    }

    public void testConstruct_2() {
        signals = new TotalIonSignals(10, null);
        assertNull("getChromatogram", signals.getChromatogram());
    }

    public void testConstruct_3() {
        signals = new TotalIonSignals(20, 40, chromatogram);
        assertNotNull("getChromatogram", signals.getChromatogram());
    }

    public void testConstruct_4() {
        signals = new TotalIonSignals(20, 40, null);
        assertNull("getChromatogram", signals.getChromatogram());
    }
}
