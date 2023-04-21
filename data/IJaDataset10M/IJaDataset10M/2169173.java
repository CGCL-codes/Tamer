package org.systemsbiology.utils;

import junit.framework.TestCase;

/**
 * JUnit test class for RobustMath
 * 
 * @author M. Vogelzang
 */
public class RobustMathTest extends TestCase {

    public void testRobustEstimate() {
        Double[] values = new Double[120];
        for (int i = 0; i < 90; i++) values[i] = new Double((Math.random() - 0.5) * 10 + 50);
        for (int i = 90; i < 120; i++) if (Math.random() > 0.5) values[i] = new Double((Math.random() - 0.5) * 40 + 200); else values[i] = new Double((Math.random() - 0.5) * 40 - 200);
        RobustMath.NormalDistributionParameters ndp = RobustMath.robustEstimate(values, 3, 2.0);
        assertEquals(50, ndp.mean, 0.8);
        assertEquals(4, ndp.stddev, 2);
    }
}
