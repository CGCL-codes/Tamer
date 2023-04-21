package net.openchrom.chromatogram.msd.model.implementation;

import java.util.TreeMap;
import java.util.Map.Entry;

public class DefaultPeakModel_6_Test extends DefaultPeakModelTestCase {

    protected float startBackgroundAbundance = 1000.0f;

    protected float stopBackgroundAbundance = 1000.0f;

    @Override
    protected void setUp() throws Exception {
        peakMaximum = new DefaultPeakMassSpectrum();
        fragmentValues = new TreeMap<Float, Float>();
        fragmentValues.put(104.0f, 230000.0f);
        fragmentValues.put(103.0f, 58000.0f);
        fragmentValues.put(51.0f, 26000.0f);
        fragmentValues.put(50.0f, 48000.0f);
        fragmentValues.put(78.0f, 23600.0f);
        fragmentValues.put(77.0f, 2500.0f);
        fragmentValues.put(74.0f, 38000.0f);
        fragmentValues.put(105.0f, 97000.0f);
        for (Entry<Float, Float> entry : fragmentValues.entrySet()) {
            ion = new DefaultPeakIon(entry.getKey(), entry.getValue());
            peakMaximum.addIon(ion);
        }
        super.setUp(startBackgroundAbundance, stopBackgroundAbundance, peakMaximum);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetBackgroundAbundance_1() {
        assertEquals("GetBackgroundAbundance", 1000.0f, peakModel.getBackgroundAbundance(1500));
    }

    public void testGetBackgroundAbundance_2() {
        assertEquals("GetBackgroundAbundance", 1000.0f, peakModel.getBackgroundAbundance(7800));
    }

    public void testGetBackgroundAbundance_3() {
        assertEquals("GetBackgroundAbundance", 1000.0f, peakModel.getBackgroundAbundance(15500));
    }

    public void testGetBackgroundAbundance_4() {
        assertEquals("GetBackgroundAbundance", 1000.0f, peakModel.getBackgroundAbundance());
    }

    public void testGetPeakAbundance_1() {
        assertEquals("GetPeakAbundance", 523100.00f, peakModel.getPeakAbundance());
    }

    public void testGetPeakAbundanceByInflectionPoints_1() {
        assertEquals("GetPeakAbundanceByInflectionPoints", 584789.75f, peakModel.getPeakAbundanceByInflectionPoints());
    }

    public void testGetWidthBaselineTotal_1() {
        assertEquals("GetWidthBaselineTotal", 14001, peakModel.getWidthBaselineTotal());
    }

    public void testGetWidthBaselineByInflectionPoints_1() {
        assertEquals("GetWidthBaselineByInflectionPoints", 8187, peakModel.getWidthBaselineByInflectionPoints());
    }

    public void testGetWidthByInflectionPoints_1() {
        assertEquals("GetWidthByInflectionPoints", 4094, peakModel.getWidthByInflectionPoints());
    }

    public void testGetWidthByInflectionPoints_2() {
        assertEquals("GetWidthByInflectionPoints 50%", 4094, peakModel.getWidthByInflectionPoints(0.5f));
    }

    public void testGetWidthByInflectionPoints_3() {
        assertEquals("GetWidthByInflectionPoints 80%", 1638, peakModel.getWidthByInflectionPoints(0.8f));
    }

    public void testGetWidthByInflectionPoints_4() {
        assertEquals("GetWidthByInflectionPoints -10%", 0, peakModel.getWidthByInflectionPoints(-0.1f));
    }

    public void testGetWidthByInflectionPoints_5() {
        assertEquals("GetWidthByInflectionPoints 110%", 0, peakModel.getWidthByInflectionPoints(1.1f));
    }

    public void testGetStartRetentionTime_1() {
        assertEquals("GetStartRetentionTime", 1500, peakModel.getStartRetentionTime());
    }

    public void testGetStopRetentionTime_1() {
        assertEquals("GetStopRetentionTime", 15500, peakModel.getStopRetentionTime());
    }

    public void testGetRetentionTimeAtPeakMaximum_1() {
        assertEquals("GetRetentionTimeAtPeakMaximum", 9500, peakModel.getRetentionTimeAtPeakMaximum());
    }

    public void testGetRetentionTimeAtPeakMaximumByInflectionPoints_1() {
        assertEquals("GetRetentionTimeAtPeakMaximumByInflectionPoints", 9327, peakModel.getRetentionTimeAtPeakMaximumByInflectionPoints());
    }

    public void testGradientAngle_1() {
        assertEquals("GradientAngle", 0.0d, peakModel.getGradientAngle());
    }

    public void testGetIncreasingInflectionPointAbundance_1() {
        assertEquals("GetIncreasingInflectionPointAbundance", -889270.0f, peakModel.getIncreasingInflectionPointAbundance(1500));
    }

    public void testGetIncreasingInflectionPointAbundance_2() {
        assertEquals("GetIncreasingInflectionPointAbundance", 297120.8f, peakModel.getIncreasingInflectionPointAbundance(7800));
    }

    public void testGetIncreasingInflectionPointAbundance_3() {
        assertEquals("GetIncreasingInflectionPointAbundance", 1747154.0f, peakModel.getIncreasingInflectionPointAbundance(15500));
    }

    public void testGetDecreasingInflectionPointAbundance_1() {
        assertEquals("GetDecreasingInflectionPointAbundance", 1485604.0f, peakModel.getDecreasingInflectionPointAbundance(1500));
    }

    public void testGetDecreasingInflectionPointAbundance_2() {
        assertEquals("GetDecreasingInflectionPointAbundance", 760587.4f, peakModel.getDecreasingInflectionPointAbundance(7800));
    }

    public void testGetDecreasingInflectionPointAbundance_3() {
        assertEquals("GetDecreasingInflectionPointAbundance", -125544.0f, peakModel.getDecreasingInflectionPointAbundance(15500));
    }
}
