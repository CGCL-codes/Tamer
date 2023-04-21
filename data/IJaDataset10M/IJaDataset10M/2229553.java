package com.tomgibara.mosey.coding;

import java.util.Random;
import com.tomgibara.mosey.portal.Portal;
import junit.framework.TestCase;

public class PortalTest extends TestCase {

    public void testChecksumDiffers() {
        Portal p1 = new Portal(24, (short) 16, -1);
        Portal p2 = new Portal(25, (short) 16, -1);
        assertFalse(p1.getChecksum() == p2.getChecksum());
    }

    public void testZeroChecksum() {
        Portal p = new Portal(0, (short) 0, 0);
        System.out.println(Integer.toBinaryString(p.getChecksum()));
    }

    public void testLongDataPositive() {
        Portal p = new Portal(0, (short) -14232, -235406676);
        assertTrue(p.getLongData() >= 0);
    }

    public void testLongDataMatches() {
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            Portal p = new Portal(i, (short) r.nextInt(), r.nextInt());
            Portal q = new Portal(p.getChamberId(), p.getLongData());
            assertEquals(p, q);
        }
    }

    public void testUnsignedString() {
        assertEquals("18446744073709551615", Portal.toUnsignedString(-1L));
        assertEquals("4294967295", Portal.toUnsignedString((int) 4294967295L));
        assertEquals("65535", Portal.toUnsignedString((short) 65535));
    }

    public void testHexValues() {
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            Portal p = new Portal(r.nextInt(), (short) r.nextInt(), r.nextInt());
            String[] chamber = { p.getChamberHex(false), p.getChamberHex(true) };
            String[] data = { p.getDataHex(false, false), p.getDataHex(true, false), p.getDataHex(false, true), p.getDataHex(true, true) };
            assertEquals(8, chamber[1].length());
            assertEquals(12, data[1].length());
            assertEquals(13, data[3].length());
            assertEquals(data[1], data[3].replace(":", ""));
            for (int j = 0; j < chamber.length; j++) {
                for (int k = 0; k < data.length; k++) {
                    assertEquals(p, Portal.fromHex(chamber[j], data[k]));
                }
            }
        }
    }
}
