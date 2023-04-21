package jaxlib.array;

import java.util.Random;
import jaxlib.lang.Longs;
import jaxlib.junit.XTestCase;

/**
 * TODO: comment
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: LongBitArraysTest.java 2267 2007-03-16 08:33:33Z joerg_wassmer $
 */
public final class LongBitArraysTest extends BitArraysTestCase<long[]> {

    public LongBitArraysTest(String name) {
        super(name);
    }

    @Override
    void assertBitsEqual(boolean[] expected, long[] current) {
        assertEquals(expected.length, current.length << Longs.ADDRESS_BITS);
        for (int i = expected.length; --i >= 0; ) assertEquals(expected[i], LongBitArrays.get(current, i));
    }

    @Override
    long[] createBits() {
        long[] a = new long[257];
        for (int i = a.length; --i >= 0; ) a[i] = (i << 16) + i;
        return a;
    }

    @Override
    Class<long[]> getArrayClass() {
        return long[].class;
    }

    @Override
    int getBitsPerUnit() {
        return Longs.BITS;
    }

    @Override
    Class getComponentType() {
        return long.class;
    }

    @Override
    Class getTestClass() {
        return LongBitArrays.class;
    }

    public void testCount() {
        long[] a = createBits();
        boolean[] b = LongBitArrays.toBooleanArray(a);
        assertEquals(BooleanArrays.count(b, true), LongBitArrays.count(a, true));
        assertEquals(BooleanArrays.count(b, false), LongBitArrays.count(a, false));
    }

    public void testFill() {
        long[] a = new long[100];
        LongBitArrays.fill(a, true);
        assertTrue(LongArrays.count(a, ~0L) == a.length);
        LongBitArrays.fill(a, false);
        assertTrue(LongArrays.count(a, 0L) == a.length);
        for (int i = 0; i < a.length * 64; i++) assertEquals(false, LongBitArrays.get(a, i));
        LongBitArrays.fill(a, 10, 20, true);
        for (int i = 0; i < 10; i++) assertEquals(false, LongBitArrays.get(a, i));
        for (int i = 10; i < 20; i++) assertEquals(true, LongBitArrays.get(a, i));
        for (int i = 20; i < a.length * 64; i++) assertEquals(false, LongBitArrays.get(a, i));
    }

    public void testFlip() {
        long[] a = createBits();
        for (int i = a.length << Longs.ADDRESS_BITS; --i >= 0; ) {
            boolean v = LongBitArrays.get(a, i);
            LongBitArrays.flip(a, i);
            assertEquals(!v, LongBitArrays.get(a, i));
        }
        long[] b = createBits();
        for (int i = a.length << Longs.ADDRESS_BITS; --i >= 0; ) assertEquals(!LongBitArrays.get(b, i), LongBitArrays.get(a, i));
    }

    public void testFlipRange() {
        long[] a = createBits();
        long[] b = createBits();
        LongBitArrays.flip(a);
        for (int i = a.length << Longs.ADDRESS_BITS; --i >= 0; ) assertEquals(!LongBitArrays.get(b, i), LongBitArrays.get(a, i));
        a = createBits();
        LongBitArrays.flip(a, 512, 917);
        for (int i = 512; i < 917; i++) assertEquals(!LongBitArrays.get(b, i), LongBitArrays.get(a, i));
        for (int i = 0; i < 512; i++) assertEquals(LongBitArrays.get(b, i), LongBitArrays.get(a, i));
        for (int i = 917; i < a.length << Longs.ADDRESS_BITS; i++) assertEquals(LongBitArrays.get(b, i), LongBitArrays.get(a, i));
    }
}
