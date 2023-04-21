package com.triplyx.volume;

import junit.framework.TestCase;
import java.io.IOException;

public class DecryptingInputStreamTest extends TestCase {

    public void testRefusesNullInputStream1() throws IOException {
        try {
            new DecryptingInputStream(null, new ReadonlyThirdVolume(new byte[8], ThirdVolume.MEMBER_D2B_AND_A, 8));
            fail("Did not throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testRefusesNullInputStream2() throws IOException {
        try {
            new DecryptingInputStream(new ReadonlyThirdVolume(new byte[8], ThirdVolume.MEMBER_D2B_AND_A, 8), null);
            fail("Did not throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testRefusesMismatchedStripeSizes() throws IOException {
        try {
            new DecryptingInputStream(new ReadonlyThirdVolume(new byte[8], ThirdVolume.MEMBER_D1A_AND_B, 6), new ReadonlyThirdVolume(new byte[8], ThirdVolume.MEMBER_D2B_AND_A, 8));
            fail("Did not reject mismatched stripe sizes");
        } catch (IllegalArgumentException e) {
        }
    }
}
