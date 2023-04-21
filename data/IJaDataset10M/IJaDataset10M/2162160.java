package java.awt.color;

import junit.framework.TestCase;

public class ICC_ProfileRTest extends TestCase {

    public void testGetInstance() {
        try {
            byte[] ba = new byte[] { (byte) 0x58, (byte) 0x59, (byte) 0x5A, (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
            ICC_Profile.getInstance(ba);
            fail("IllegalArgumentExceptione expected");
        } catch (IllegalArgumentException e) {
        }
    }
}
