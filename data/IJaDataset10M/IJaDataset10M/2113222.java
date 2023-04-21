package net.openchrom.chromatogram.msd.peak.detector.supplier.chemstation.core;

import junit.framework.TestCase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.openchrom.chromatogram.msd.peak.detector.supplier.chemstation.core.ChemStationPeakDetector;
import net.openchrom.chromatogram.msd.peak.support.IRawPeak;
import net.openchrom.chromatogram.msd.peak.support.RawPeak;

/**
 * peakDetectorSettings.getThreshold() is MEDIUM > threshold = 0.05d;
 * WindowSize.SCANS_5
 * 
 * @author eselmeister
 */
public class ChemStationPeakDetector_3_Test extends TestCase {

    private ChemStationPeakDetector chemStationPeakDetector;

    private Class<?> chemStationPeakDetectorClass;

    private Method method;

    private IRawPeak rawPeak;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        chemStationPeakDetector = new ChemStationPeakDetector();
        chemStationPeakDetectorClass = ChemStationPeakDetector.class;
    }

    @Override
    protected void tearDown() throws Exception {
        chemStationPeakDetector = null;
        chemStationPeakDetectorClass = null;
        super.tearDown();
    }

    public void testIsValidRawPeak_1() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        rawPeak = new RawPeak(25, 26, 27);
        Boolean result;
        method = chemStationPeakDetectorClass.getDeclaredMethod("isValidRawPeak", new Class[] { IRawPeak.class });
        method.setAccessible(true);
        result = (Boolean) method.invoke(chemStationPeakDetector, new Object[] { rawPeak });
        assertEquals("detectPeakStart", Boolean.valueOf(true), result);
    }

    public void testIsValidRawPeak_2() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        rawPeak = new RawPeak(25, 25, 26);
        Boolean result;
        method = chemStationPeakDetectorClass.getDeclaredMethod("isValidRawPeak", new Class[] { IRawPeak.class });
        method.setAccessible(true);
        result = (Boolean) method.invoke(chemStationPeakDetector, new Object[] { rawPeak });
        assertEquals("detectPeakStart", Boolean.valueOf(false), result);
    }
}
