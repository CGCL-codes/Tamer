package org.apache.lucene.util;

import java.util.Random;

/**
 * @version $Id$
 */
public class TestSmallFloat extends LuceneTestCase {

    static float orig_byteToFloat(byte b) {
        if (b == 0) return 0.0f;
        int mantissa = b & 7;
        int exponent = (b >> 3) & 31;
        int bits = ((exponent + (63 - 15)) << 24) | (mantissa << 21);
        return Float.intBitsToFloat(bits);
    }

    static byte orig_floatToByte(float f) {
        if (f < 0.0f) f = 0.0f;
        if (f == 0.0f) return 0;
        int bits = Float.floatToIntBits(f);
        int mantissa = (bits & 0xffffff) >> 21;
        int exponent = (((bits >> 24) & 0x7f) - 63) + 15;
        if (exponent > 31) {
            exponent = 31;
            mantissa = 7;
        }
        if (exponent < 0) {
            exponent = 0;
            mantissa = 1;
        }
        return (byte) ((exponent << 3) | mantissa);
    }

    public void testByteToFloat() {
        for (int i = 0; i < 256; i++) {
            float f1 = orig_byteToFloat((byte) i);
            float f2 = SmallFloat.byteToFloat((byte) i, 3, 15);
            float f3 = SmallFloat.byte315ToFloat((byte) i);
            assertEquals(f1, f2, 0.0);
            assertEquals(f2, f3, 0.0);
            float f4 = SmallFloat.byteToFloat((byte) i, 5, 2);
            float f5 = SmallFloat.byte52ToFloat((byte) i);
            assertEquals(f4, f5, 0.0);
        }
    }

    public void testFloatToByte() {
        Random rand = newRandom();
        for (int i = 0; i < 100000; i++) {
            float f = Float.intBitsToFloat(rand.nextInt());
            if (f != f) continue;
            byte b1 = orig_floatToByte(f);
            byte b2 = SmallFloat.floatToByte(f, 3, 15);
            byte b3 = SmallFloat.floatToByte315(f);
            assertEquals(b1, b2);
            assertEquals(b2, b3);
            byte b4 = SmallFloat.floatToByte(f, 5, 2);
            byte b5 = SmallFloat.floatToByte52(f);
            assertEquals(b4, b5);
        }
    }
}
