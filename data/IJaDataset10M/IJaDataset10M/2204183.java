package net.sf.jctools.ant;

import net.sf.jctools.JCToolsTestCase;

public class AIDTest extends JCToolsTestCase {

    /**
   * @see AID#parseAID(String)
   */
    public void testParseAID() {
        assertEquals(new byte[] { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9A, (byte) 0xBC, (byte) 0xDE }, AID.parseAID("0x12:0x34:0x56:0x78:0x9A:0xBC:0xDE"));
        assertEquals(new byte[] { (byte) 0x5F }, AID.parseAID("0x5F"));
        try {
            AID.parseAID("");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
        try {
            AID.parseAID("0xAB:1xCD");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
        try {
            AID.parseAID("0xAB:0yCD");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
        try {
            AID.parseAID("0xAB:0");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
        try {
            AID.parseAID("0x1X");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
        try {
            AID.parseAID("0x1");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
        try {
            AID.parseAID("0xAB:0xCD:");
            failNoException();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
   * @see AID#AID(String)
   */
    public void testAID() {
        assertEquals(new byte[] { 0x12, 0x34, 0x56 }, new AID("0x12:0x34:0x56").getBytes());
    }

    /**
   * @see AID#equals(Object)
   */
    public void testEquals() {
        AID aid1 = new AID("0x12:0xAB:0x34:0xCD");
        AID aid2 = new AID("0x12:0xAB:0x34:0xCD");
        AID aid3 = new AID("0x12:0xAB:0x34:0xCF");
        AID aid4 = new AID("0x12:0xAB:0x34:0xCD:0x56");
        AID aid5 = new AID("0x12:0xAB:0x34");
        assertTrue(aid1.equals(aid1));
        assertTrue(aid1.equals(aid2));
        assertFalse(aid1.equals(aid3));
        assertFalse(aid1.equals(aid4));
        assertFalse(aid1.equals(aid5));
        assertFalse(aid1.equals(new Object()));
    }

    /**
   * @see AID#toString()
   */
    public void testToString() {
        assertEquals("0x12:0xAB:0x34:0xCD", new AID("0x12:0xAb:0x34:0xcD").toString());
    }
}
