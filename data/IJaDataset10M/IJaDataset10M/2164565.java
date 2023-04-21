package net.sourceforge.juint;

import net.sourceforge.juint.Int16;
import junit.framework.TestCase;

public class TestInt16 extends TestCase {

    public void testByteConstructor() {
        byte[] word = new byte[2];
        word[0] = (byte) 0x83;
        word[1] = (byte) 0x00;
        Int16 int16 = new Int16(word);
        assertEquals(-32000, int16.int16Value());
    }

    public void testShortConstructor() {
        Int16 int16 = new Int16((short) 32000);
        assertEquals(32000, int16.int16Value());
    }

    public void testIntConstructor() {
        Int16 int16 = new Int16(30000);
        assertEquals(30000, int16.int16Value());
    }

    public void testLongConstructor() {
        Int16 int16 = new Int16(1000L);
        assertEquals(1000, int16.int16Value());
    }

    public void testIntConstructorOverflow() {
        Int16 int16 = new Int16(Integer.MAX_VALUE);
        assertEquals(-1, int16.int16Value());
    }

    public void testLongConstructorOverflow() {
        Int16 int16 = new Int16(Long.MAX_VALUE);
        assertEquals(-1, int16.int16Value());
    }

    public void testConstructorException1() {
        byte[] word = new byte[3];
        word[0] = 1;
        word[1] = 2;
        word[2] = 3;
        try {
            Int16 int16 = new Int16(word);
            int16.byteValue();
            fail("Should raise an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testConstructorException2() {
        try {
            Int16 int16 = new Int16(null);
            int16.byteValue();
            fail("Should raise an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testValueOfBigEndian() {
        byte[] word = new byte[2];
        word[0] = (byte) 0xca;
        word[1] = (byte) 0x93;
        Int16 int16 = Int16.valueOfBigEndian(word);
        assertEquals(-13677, int16.int16Value());
    }

    public void testValueOfLittleEndian() {
        byte[] word = new byte[2];
        word[0] = (byte) 0xca;
        word[1] = (byte) 0x93;
        Int16 int16 = Int16.valueOfLittleEndian(word);
        assertEquals(-27702, int16.int16Value());
    }

    public void testMaxValue() {
        Int16 int16 = new Int16(32767);
        assertEquals(Int16.MAX_VALUE, int16.int16Value());
    }

    public void testShortValue() {
        Int16 int16 = new Int16(1000);
        assertEquals(1000, int16.shortValue());
    }

    public void testIntValue() {
        Int16 int16 = new Int16(20000);
        assertEquals(20000, int16.intValue());
    }

    public void testLongValue() {
        Int16 int16 = new Int16(20001);
        assertEquals(20001, int16.longValue());
    }

    public void testFloatValue() {
        Int16 int16 = new Int16(20002);
        assertEquals(20002.0f, int16.floatValue(), 0);
    }

    public void testDoubleValue() {
        Int16 int16 = new Int16(1978);
        assertEquals(1978.0d, int16.doubleValue(), 0);
    }

    public void testCompareToMore() {
        Int16 one = new Int16(60001);
        Int16 two = new Int16(60010);
        assertEquals(-9, one.compareTo(two));
    }

    public void testCompareToLess() {
        Int16 one = new Int16(60010);
        Int16 two = new Int16(60001);
        assertEquals(9, one.compareTo(two));
    }

    public void testEquals() {
        Int16 one = new Int16(65000);
        Int16 two = new Int16(65000);
        assertTrue(one.equals(two));
    }

    public void testToString() {
        Int16 int16 = new Int16(-578);
        assertEquals("-578", int16.toString());
    }

    public void testByteValue() {
        byte[] word = new byte[2];
        word[0] = (byte) 0xff;
        word[1] = 0x0a;
        Int16 int16 = new Int16(word);
        assertEquals(10, int16.byteValue());
    }

    public void testToBigEndian() {
        Int16 int16 = new Int16(2453);
        byte[] bigEndian = int16.toBigEndian();
        assertEquals(0x09, bigEndian[0]);
        assertEquals((byte) 0x95, bigEndian[1]);
    }

    public void testToLittleEndian() {
        Int16 int16 = new Int16(2453);
        byte[] littleEndian = int16.toLittleEndian();
        assertEquals((byte) 0x95, littleEndian[0]);
        assertEquals(0x09, littleEndian[1]);
    }

    public void testCompareToEquals() {
        Int16 one = new Int16((byte) 1);
        Int16 two = new Int16((byte) 1);
        assertEquals(0, one.compareTo(two));
    }

    public void testHashCode() {
        Int16 Int16 = new Int16((byte) 10);
        assertEquals(Int16.intValue(), Int16.hashCode());
    }

    public void testAnd() {
        Int16 one = new Int16(0xab);
        Int16 two = new Int16(0xff);
        assertEquals(new Int16(0xab), one.and(two));
    }

    public void testNot() {
        Int16 int16 = new Int16(0xab);
        assertEquals(new Int16(0xff54), int16.not());
    }

    public void testOr() {
        Int16 one = new Int16(0xab);
        Int16 two = new Int16(0xf0);
        assertEquals(new Int16(0xfb), one.or(two));
    }

    public void testXor() {
        Int16 one = new Int16(0xab);
        Int16 two = new Int16(0xf0);
        assertEquals(new Int16(0x5b), one.xor(two));
    }

    public void testAddBitmask() {
        Int16 int16 = new Int16(0x02);
        assertEquals(new Int16(0x06), int16.addBitmask(new Int16(0x04)));
    }

    public void testHasBitmask() {
        Int16 int16 = new Int16(0x06);
        assertTrue(int16.hasBitmask(new Int16(0x04)));
    }

    public void testRemoveBitmask() {
        Int16 int16 = new Int16(0x06);
        assertEquals(new Int16(0x02), int16.removeBitmask(new Int16(0x04)));
    }
}
