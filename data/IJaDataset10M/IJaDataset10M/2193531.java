package net.openchrom.chromatogram.msd.model.core.support;

import org.easymock.EasyMock;
import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.core.IPeak;
import net.openchrom.chromatogram.msd.model.core.ISupplierMassSpectrum;
import net.openchrom.chromatogram.msd.model.implementation.DefaultSupplierIon;
import net.openchrom.chromatogram.msd.model.implementation.DefaultSupplierMassSpectrum;
import junit.framework.TestCase;

public class ChromatogramSelection_8_Test extends TestCase {

    private IChromatogram chromatogram;

    private IChromatogramSelectionSetter selection;

    private ISupplierMassSpectrum scan;

    private IPeak peak;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        chromatogram = EasyMock.createNiceMock(IChromatogram.class);
        EasyMock.expect(chromatogram.getStartRetentionTime()).andStubReturn(1);
        EasyMock.expect(chromatogram.getStopRetentionTime()).andStubReturn(100);
        EasyMock.expect(chromatogram.getMaxSignal()).andStubReturn(127500.0f);
        scan = new DefaultSupplierMassSpectrum();
        scan.setRetentionTime(4500);
        scan.addIon(new DefaultSupplierIon(45.0f, 2883.9f));
        peak = EasyMock.createNiceMock(IPeak.class);
        EasyMock.expect(peak.getIntegratedArea()).andStubReturn(893002.3d);
        EasyMock.replay(peak);
        EasyMock.replay(chromatogram);
        selection = new ChromatogramSelection(chromatogram);
    }

    @Override
    protected void tearDown() throws Exception {
        chromatogram = null;
        selection = null;
        peak = null;
        scan = null;
        super.tearDown();
    }

    public void testSetSelectedScan_1() {
        selection.setSelectedScan(null);
        assertNull(selection.getSelectedScan());
    }

    public void testSetSelectedScan_2() {
        selection.setSelectedScan(scan);
        scan = selection.getSelectedScan();
        assertNotNull(selection.getSelectedScan());
        assertEquals("RetentionTime", 4500, scan.getRetentionTime());
    }

    public void testSetSelectedPeak_1() {
        selection.setSelectedPeak(null);
        assertNull(selection.getSelectedPeak());
    }

    public void testSetSelectedPeak_2() {
        selection.setSelectedPeak(peak);
        peak = selection.getSelectedPeak();
        assertNotNull(peak);
        assertEquals("IntegratedArea", 893002.3d, peak.getIntegratedArea());
    }
}
