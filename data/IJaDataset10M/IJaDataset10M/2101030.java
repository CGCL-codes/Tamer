package org.jd3lib.util;

import junit.framework.TestCase;

/**
 * @author Andreas Grunewald
 * 
 * LATER 1.0 Write Documentation
 */
public class CoderTest extends TestCase {

    byte[] test0 = { 0, 0, 2, 1 };

    int t1 = 257;

    byte[] test1 = { 0x0, 0x0, 0x1, 0x76 };

    int t2 = 246;

    byte[] test2 = { 0x0, 0x0, 0x0d, 0x4e };

    int t3 = 1742;

    byte[] test3 = { 0x0, 0x0, 0x10, 0x49 };

    int t4 = 2121;

    int[] t = { t1, t2, t3, t4 };

    byte[][] test = { test0, test1, test2, test3 };

    public void testGetSyncsafeIntegerValue() {
        assertEquals(t[0], Coder.getSyncsafeIntegerValue(test[0]));
        assertEquals(t[1], Coder.getSyncsafeIntegerValue(test[1]));
        assertEquals(t[2], Coder.getSyncsafeIntegerValue(test[2]));
        assertEquals(t[3], Coder.getSyncsafeIntegerValue(test[3]));
    }

    public void testGetSyncsafeIntegerValueSpeedTest() {
        for (int i = 0; i <= 40000; i++) assertEquals(t[i % 4], Coder.getSyncsafeIntegerValue(test[i % 4]));
    }

    public void testGetSyncsafeBytes() {
        for (int j = 0; j < test.length; j++) {
            byte[] testVal = Coder.getSyncsafeBytes(t[j]);
            for (int i = 0; i < test[j].length; i++) {
                assertEquals(test[j][i], testVal[i]);
            }
        }
    }

    public void testGetNextBlockAlign() {
        int[] val = { 0, 1, 510, 512, 513, 1026, 2043, 2048 };
        int[] valC = { 512, 512, 512, 512, 1024, 1536, 2048, 2048 };
        for (int i = 0; i < val.length; i++) {
            assertEquals(0, Coder.getNextBlockAlign(val[i]) % 512);
            assertEquals(valC[i], Coder.getNextBlockAlign(val[i]));
        }
    }
}
