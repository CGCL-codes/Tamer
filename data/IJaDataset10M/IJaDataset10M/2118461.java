package org.tranche.server.logs;

import org.tranche.util.DevUtil;
import org.tranche.commons.RandomUtil;
import org.tranche.util.TrancheTestCase;

/**
 *
 * @author Bryan E. Smith - bryanesmith@gmail.com
 */
public class LogUtilTest extends TrancheTestCase {

    /**
     * Test of getIPVersion method, of class org.tranche.server.logs.LogUtil.
     */
    public void testGetIPVersion() {
        assertEquals("Expecting correct version.", ActionByte.IPv4, LogUtil.getIPVersion("127.0.0.1"));
        assertEquals("Expecting correct version.", ActionByte.IPv6, LogUtil.getIPVersion("2001:0db8:85a3:08d3:1319:8a2e:0370:7344"));
        assertEquals("Expecting correct version.", ActionByte.IPv4, LogUtil.getIPVersion("255.255.255.255"));
        assertEquals("Expecting correct version.", ActionByte.IPv6, LogUtil.getIPVersion("2001:0db8:0000:0000:0000:0000:1428:57ab"));
        assertEquals("Expecting correct version.", ActionByte.IPv4, LogUtil.getIPVersion("192.0.2.235"));
        assertEquals("Expecting correct version.", ActionByte.IPv6, LogUtil.getIPVersion("2001:db8::1428:57ab"));
        try {
            LogUtil.getIPVersion("");
            fail("Should throw exception");
        } catch (Exception ex) {
        }
        try {
            LogUtil.getIPVersion("127.0.0");
            fail("Should throw exception");
        } catch (Exception ex) {
        }
        try {
            LogUtil.getIPVersion("I must fail!");
            fail("Should throw exception");
        } catch (Exception ex) {
        }
        try {
            LogUtil.getIPVersion("127001");
            fail("Should throw exception");
        } catch (Exception ex) {
        }
    }

    /**
     * Test of getIPv4Bytes method, of class org.tranche.server.logs.LogUtil.
     */
    public void testGetIPv4Bytes() {
        String addr = "127.0.0.1";
        byte[] expected = new byte[4];
        expected[0] = Byte.MIN_VALUE + 127;
        expected[1] = Byte.MIN_VALUE + 0;
        expected[2] = Byte.MIN_VALUE + 0;
        expected[3] = Byte.MIN_VALUE + 1;
        byte[] found = LogUtil.getIPv4Bytes(addr);
        for (int i = 0; i < 4; i++) {
            assertEquals("Expecting " + expected[i] + ", found " + found[i], expected[i], found[i]);
        }
        addr = "0.0.0.0";
        expected[0] = Byte.MIN_VALUE + 0;
        expected[1] = Byte.MIN_VALUE + 0;
        expected[2] = Byte.MIN_VALUE + 0;
        expected[3] = Byte.MIN_VALUE + 0;
        found = LogUtil.getIPv4Bytes(addr);
        for (int i = 0; i < 4; i++) {
            assertEquals("Expecting " + expected[i] + ", found " + found[i], expected[i], found[i]);
        }
        addr = "255.255.255.255";
        expected[0] = Byte.MIN_VALUE + 255;
        expected[1] = Byte.MIN_VALUE + 255;
        expected[2] = Byte.MIN_VALUE + 255;
        expected[3] = Byte.MIN_VALUE + 255;
        found = LogUtil.getIPv4Bytes(addr);
        for (int i = 0; i < 4; i++) {
            assertEquals("Expecting " + expected[i] + ", found " + found[i], expected[i], found[i]);
        }
        int r1 = RandomUtil.getInt(256), r2 = RandomUtil.getInt(256), r3 = RandomUtil.getInt(256), r4 = RandomUtil.getInt(256);
        addr = r1 + "." + r2 + "." + r3 + "." + r4;
        expected[0] = (byte) (Byte.MIN_VALUE + r1);
        expected[1] = (byte) (Byte.MIN_VALUE + r2);
        expected[2] = (byte) (Byte.MIN_VALUE + r3);
        expected[3] = (byte) (Byte.MIN_VALUE + r4);
        found = LogUtil.getIPv4Bytes(addr);
        for (int i = 0; i < 4; i++) {
            assertEquals("Expecting " + expected[i] + ", found " + found[i], expected[i], found[i]);
        }
    }

    public void testMD5HashingOfCertificate() throws Exception {
        byte[] md5Bytes = LogUtil.createCertificateMD5(DevUtil.getDevAuthority());
        assertEquals("Expecting 16 bytes.", 16, md5Bytes.length);
    }

    public void testTimestampBytes() throws Exception {
        long expected = 0;
        byte[] valBytes = LogUtil.convertLongToBytes(expected);
        long found = LogUtil.convertBytesToLong(valBytes);
        assertEquals("Expecting eight bytes.", 8, valBytes.length);
        assertEquals("Expecting longs to match.", expected, found);
        expected = Long.MIN_VALUE;
        valBytes = LogUtil.convertLongToBytes(expected);
        found = LogUtil.convertBytesToLong(valBytes);
        assertEquals("Expecting eight bytes.", 8, valBytes.length);
        assertEquals("Expecting longs to match.", expected, found);
        expected = Long.MAX_VALUE;
        valBytes = LogUtil.convertLongToBytes(expected);
        found = LogUtil.convertBytesToLong(valBytes);
        assertEquals("Expecting eight bytes.", 8, valBytes.length);
        assertEquals("Expecting longs to match.", expected, found);
        expected = RandomUtil.getLong();
        valBytes = LogUtil.convertLongToBytes(expected);
        found = LogUtil.convertBytesToLong(valBytes);
        assertEquals("Expecting eight bytes.", 8, valBytes.length);
        assertEquals("Expecting longs to match.", expected, found);
    }

    /**
     * Create random data and checksum, make sure validates. Also perform
     * couple negative tests for which the checksum should fail.
     */
    public void testXORChecksum() throws Exception {
        for (int i = 0; i < 2 * 256; i++) {
            runXORChecksumTest();
        }
    }

    /**
     * Helper method to test 1 single positive and negative XOR checksum test.
     */
    public void runXORChecksumTest() throws Exception {
        int size = 50 + RandomUtil.getInt(150);
        byte[] data = new byte[size];
        RandomUtil.getBytes(data);
        byte checksum = LogUtil.createXORChecksum(data);
        assertTrue("Checksum should validate.", LogUtil.validateXORChecksum(checksum, data));
        assertFalse("Checksum should not validate, changed checksum value.", LogUtil.validateXORChecksum((byte) (checksum + 1), data));
        byte[] data2 = new byte[size];
        int count = 0;
        while (true) {
            RandomUtil.getBytes(data2);
            if (count > 100) {
                throw new Exception("Tried 100 times to find a payload with a different checksum, bailing...");
            }
            count++;
            if (LogUtil.createXORChecksum(data2) != checksum) {
                break;
            }
        }
        assertFalse("Checksum should not validate, changed data.", LogUtil.validateXORChecksum(checksum, data2));
    }
}
