package org.dyn4j;

import org.dyn4j.UnitConversion;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * Test case for the {@link UnitConversion} class.
 * @author William Bittle
 * @version 1.0.3
 * @since 1.0.1
 */
public class UnitConversionTest {

    /**
	 * Tests the foot to meter and meter to foot conversions.
	 */
    @Test
    public void footMeter() {
        TestCase.assertEquals(1.000, UnitConversion.FOOT_TO_METER * UnitConversion.METER_TO_FOOT);
        double m = 2.5;
        double f = UnitConversion.metersToFeet(m);
        double r = UnitConversion.feetToMeters(f);
        TestCase.assertEquals(m, r, 1.0e-9);
    }

    /**
	 * Tests the slug to kilogram and kilogram to slug conversions.
	 */
    @Test
    public void slugKilogram() {
        TestCase.assertEquals(1.000, UnitConversion.SLUG_TO_KILOGRAM * UnitConversion.KILOGRAM_TO_SLUG);
        double s = 2.5;
        double k = UnitConversion.slugsToKilograms(s);
        double r = UnitConversion.kilogramsToSlugs(k);
        TestCase.assertEquals(s, r, 1.0e-9);
    }

    /**
	 * Tests the pound to kilogram and kilogram to pound conversions.
	 */
    @Test
    public void poundKilogram() {
        TestCase.assertEquals(1.000, UnitConversion.POUND_TO_KILOGRAM * UnitConversion.KILOGRAM_TO_POUND);
        double p = 2.5;
        double k = UnitConversion.poundsToKilograms(p);
        double r = UnitConversion.kilogramsToPounds(k);
        TestCase.assertEquals(p, r, 1.0e-9);
    }

    /**
	 * Tests the meters per second to feet per second (and reverse) conversions.
	 */
    @Test
    public void mpsToFps() {
        double fps = 2.5;
        double mps = UnitConversion.metersPerSecondToFeetPerSecond(fps);
        double r = UnitConversion.feetPerSecondToMetersPerSecond(mps);
        TestCase.assertEquals(fps, r, 1.0e-9);
    }

    /**
	 * Tests the pound to newton and newton to pound conversions.
	 */
    @Test
    public void poundNewton() {
        TestCase.assertEquals(1.000, UnitConversion.POUND_TO_NEWTON * UnitConversion.NEWTON_TO_POUND);
        double p = 2.5;
        double n = UnitConversion.poundsToNewtons(p);
        double r = UnitConversion.newtonsToPounds(n);
        TestCase.assertEquals(p, r, 1.0e-9);
    }

    /**
	 * Tests the foot-pound to newton-meter and newton-meter to foot-pound conversions.
	 */
    @Test
    public void footPoundNewtonMeter() {
        TestCase.assertEquals(1.000, UnitConversion.FOOT_POUND_TO_NEWTON_METER * UnitConversion.NEWTON_METER_TO_FOOT_POUND);
        double fp = 2.5;
        double nm = UnitConversion.footPoundsToNewtonMeters(fp);
        double r = UnitConversion.newtonMetersToFootPounds(nm);
        TestCase.assertEquals(fp, r, 1.0e-9);
    }
}
