package org.apache.harmony.unpack200.tests;

import junit.framework.TestCase;
import org.apache.harmony.pack200.Pack200Exception;
import org.apache.harmony.unpack200.SegmentOptions;

/**
 * 
 */
public class SegmentOptionsTest extends TestCase {

    public void testUnused() {
        int[] unused = new int[] { 3, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31 };
        for (int i = 0; i < unused.length; i++) {
            try {
                new SegmentOptions(1 << unused[i]);
                fail("Bit " + unused[i] + " should be unused, but it's not caught during construction");
            } catch (Pack200Exception e) {
                assertTrue(true);
            }
        }
    }
}
