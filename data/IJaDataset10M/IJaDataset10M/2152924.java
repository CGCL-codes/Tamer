package net.rptools.common.expression;

import junit.framework.TestCase;

public class RunDataTest extends TestCase {

    public void testRandomIntInt() {
        RunData runData = new RunData(null);
        for (int i = 0; i < 10000; i++) {
            int value = runData.randomInt(10);
            assertTrue(1 <= value && value <= 10);
        }
    }

    public void testRandomIntIntInt() {
        RunData runData = new RunData(null);
        for (int i = 0; i < 10000; i++) {
            int value = runData.randomInt(10, 20);
            assertTrue(String.format("Value outside range: %s", value), 10 <= value && value <= 20);
        }
    }
}
