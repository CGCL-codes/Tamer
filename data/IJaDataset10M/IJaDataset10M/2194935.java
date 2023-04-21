package com.flanderra;

import java.util.Random;
import junit.framework.TestCase;

public class BitUtilsTest extends TestCase {

    public void testFullBytes() {
        Bits bits = new Bits(new byte[] { -128, 126, 127 }, 24);
        assertEquals(3, bits.getFullBytes().length);
        assertEquals(0, bits.getRestBits().getOffset());
        for (int i = 0; i < 24; i++) {
            Bits bits2 = BitUtils._sub(bits, 0, i);
            assertEquals((i + 1) / 8, bits2.getFullBytes().length);
            assertEquals((i + 1) % 8, bits2.getRestBits().getOffset());
        }
    }

    public void testBinHex() {
        String ptnHex = "00 01 02 03 04 05";
        byte[] res2 = BitUtils._hex(ptnHex);
        String reverse2 = BitUtils._hex(res2);
        assertEquals(ptnHex.trim(), reverse2.trim());
        reverse2 = BitUtils._bin(res2);
        assertNotNull(reverse2);
    }

    public void testBitIntegers() {
        Random rint = new Random();
        for (long testValue = 0, i = 0; i <= 100000; testValue = rint.nextLong(), i++) {
            Bits result2 = BitUtils._longToBits(testValue, 64);
            String reverse2 = BitUtils._bin(result2);
            assertNotNull(reverse2);
            assertEquals(testValue, BitUtils._bitsToLong(result2, false));
            assertEquals(testValue, BitUtils._bitsToLong(result2, true));
            assertEquals(BitUtils._bitsToLong(result2, 0, 64, false), BitUtils._bitsToLong(result2, false));
            assertEquals(BitUtils._bitsToLong(result2, 0, 64, true), BitUtils._bitsToLong(result2, true));
        }
        byte[] b1 = null;
        for (long testValue = BitUtils.MIN_UI8; testValue <= BitUtils.MAX_UI8; testValue++) {
            b1 = BitUtils._bytesUI8(testValue);
            assertEquals(BitUtils._parseUI8(b1), testValue);
            b1 = BitUtils._bytesUI16(testValue);
            assertEquals(BitUtils._parseUI16(b1), testValue);
            b1 = BitUtils._bytesUI24(testValue);
            assertEquals(BitUtils._parseUI24(b1), testValue);
            b1 = BitUtils._bytesUI32(testValue);
            assertEquals(BitUtils._parseUI32(b1), testValue);
        }
        for (long testValue = BitUtils.MIN_SI8; testValue <= BitUtils.MAX_SI8; testValue++) {
            b1 = BitUtils._bytesSI8(testValue);
            assertEquals(BitUtils._parseSI8(b1), testValue);
            b1 = BitUtils._bytesSI16(testValue);
            assertEquals(BitUtils._parseSI16(b1), testValue);
            b1 = BitUtils._bytesSI32(testValue);
            assertEquals(BitUtils._parseSI32(b1), testValue);
        }
    }

    public void testBitFloats() {
        byte[] b1 = null;
        for (double testValue = BitUtils.MIN_FIXED88; testValue <= BitUtils.MAX_FIXED88; testValue++) {
            b1 = BitUtils._bytesFixed88(testValue);
            assertEquals(BitUtils._parseFixed88(b1), testValue);
            b1 = BitUtils._bytesFixed1616(testValue);
            assertEquals(BitUtils._parseFixed1616(b1), testValue);
        }
        for (double testValue = BitUtils.MIN_FLOAT16; testValue <= BitUtils.MAX_FLOAT16; testValue += 101.01) {
            double reverse = 0.0;
            b1 = BitUtils._bytesFloat32(testValue);
            reverse = BitUtils._parseFloat32(b1);
            assertEquals(testValue, reverse, testValue / 1000.0);
            b1 = BitUtils._bytesFloat64(testValue);
            reverse = BitUtils._parseFloat64(b1);
            assertEquals(testValue, reverse, testValue / 1000.0);
        }
    }

    public void testEncoded() {
        for (byte testValue = 0; testValue < 5; testValue++) {
            byte[] arr = new byte[] { testValue, (byte) (testValue + 1), (byte) (testValue + 1), (byte) (testValue + 1), (byte) (testValue + 1) };
            byte[] result = BitUtils._bytesEncodedU32(arr);
            assertNotNull(result);
            assertTrue(result.length != 0);
        }
    }

    public void testBitArrays() {
        Bits b2 = null;
        for (long testValue = BitUtils.MIN_SI8; testValue <= BitUtils.MAX_SI8; testValue++) {
            b2 = BitUtils._bitsSBArray(testValue, 8);
            assertEquals(BitUtils._parseSBArray(b2), testValue);
        }
        for (long testValue = BitUtils.MIN_UI8; testValue <= BitUtils.MAX_UI8; testValue++) {
            b2 = BitUtils._bitsUBArray(testValue, 8);
            assertEquals(BitUtils._parseUBArray(b2), testValue);
        }
        for (double testValue = BitUtils.MIN_FIXED1616; testValue <= BitUtils.MAX_FIXED1616; testValue++) {
            b2 = BitUtils._bitsFBArray(testValue, 32);
            assertEquals(BitUtils._parseFBArray(b2), testValue);
        }
    }

    public void testFlipBytes() {
        Random rint = new Random();
        for (long testValue = 0, i = 0; i <= 100000; testValue = rint.nextLong(), i++) {
            Bits result2 = BitUtils._longToBits(testValue, 64);
            assertEquals(testValue, BitUtils._bitsToLong(result2, true));
        }
    }

    public void testSubConcat() {
        Random rint = new Random();
        for (long testValue = 0, i = 0; i <= 100000; testValue = rint.nextLong(), i++) {
            Bits result2 = BitUtils._longToBits(testValue, 64);
            assertEquals(BitUtils._bin(result2), BitUtils._bin(BitUtils._concat(BitUtils._concat(BitUtils._sub(result2, 0, 19), BitUtils._sub(result2, 20, 39)), BitUtils._concat(BitUtils._sub(result2, 40, 59), BitUtils._sub(result2, 60, 63)))));
            assertEquals(BitUtils._bin(result2), BitUtils._bin(BitUtils._concat(new Bits[] { BitUtils._sub(result2, 0, 0), BitUtils._sub(result2, 1, 1), BitUtils._sub(result2, 2, 2), BitUtils._sub(result2, 3, 20), BitUtils._sub(result2, 21, 40), BitUtils._sub(result2, 41, 60), BitUtils._sub(result2, 61, 63) })));
            assertEquals(BitUtils._bin(BitUtils._sub(BitUtils._concat(result2, result2), 0, 63)), BitUtils._bin(BitUtils._sub(BitUtils._concat(result2, result2), 64, 127)));
        }
    }

    public void testMaxBitCount() {
        for (long i = -50; i < 50; i++) {
            BitUtils._calculateMaxBitCountSI(new long[] { i });
        }
        for (long i = 0; i < 100; i++) {
            long[] arr = new long[] { i };
            BitUtils._calculateMaxBitCountUI(arr);
        }
        for (long i = 0; i < 100; i++) {
            long[] arr = new long[] { i };
            assertEquals(BitUtils._calculateMaxBitCountSI(arr), BitUtils._calculateMaxBitCountUI(new long[] { i }) + 1);
        }
    }
}
