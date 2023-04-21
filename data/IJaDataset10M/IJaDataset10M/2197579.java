package test.arm.util;

import junit.framework.TestCase;
import arm.util.MathUtils;

public class MathUtilsTC extends TestCase {

    public void testFactorial() {
        assertEquals("wrong answer", 6, MathUtils.factorial(3));
    }
}
