package org.hip.kernel.bitmap.test;

import static org.junit.Assert.*;
import org.hip.kernel.bitmap.BitRow;
import org.hip.kernel.bitmap.BitRowImpl;
import org.junit.Test;

/**
 * @author Benno Luthiger
 */
public class BitRowTest {

    @Test
    public void testDo() {
        BitRow lBits1 = new BitRowImpl(8);
        assertEquals("Bit value 1", 0, lBits1.getBitValue());
        lBits1.setBit(1, true);
        assertEquals("Bit value 2", 2, lBits1.getBitValue());
        lBits1.setBit(1, false);
        assertEquals("Bit value 3", 0, lBits1.getBitValue());
        lBits1.setBitValue(8);
        assertTrue("Bit position 1", lBits1.getBit(3));
        lBits1.setBit(7, true);
        lBits1.setBitValue(8);
        assertTrue("Bit position 2", lBits1.getBit(3));
        lBits1.setBitValue(1);
        BitRow lBits2 = lBits1.invert();
        assertEquals("Bit value 4", 254, lBits2.getBitValue());
        lBits1.setBitValue(511);
        assertEquals("Bit value 5", 255, lBits1.getBitValue());
        lBits2.setBitValue(1);
        assertEquals("Bit value 6", 1, lBits1.and(lBits2).getBitValue());
        assertEquals("Bit value 7", 255, lBits1.or(lBits2).getBitValue());
        assertEquals("Bit value 8", 254, lBits1.xor(lBits2).getBitValue());
        lBits1.setBitValue(254);
        assertEquals("Bit value 9", 0, lBits1.and(lBits2).getBitValue());
        assertEquals("Bit value 10", 255, lBits1.or(lBits2).getBitValue());
        assertEquals("Bit value 11", 255, lBits1.xor(lBits2).getBitValue());
        lBits2.setBitValue(254);
        assertEquals("Bit value 12", 254, lBits1.and(lBits2).getBitValue());
        assertEquals("Bit value 13", 254, lBits1.or(lBits2).getBitValue());
        assertEquals("Bit value 14", 0, lBits1.xor(lBits2).getBitValue());
        try {
            lBits1.setBit(9, false);
            fail("Shouldn't get here!");
        } catch (ArrayIndexOutOfBoundsException exc) {
        }
        assertTrue("equal", lBits1.equals(lBits2));
        lBits2.setBitValue(10);
        assertTrue("not equal 1", !lBits1.equals(lBits2));
        assertTrue("not equal 2", !lBits1.equals("test"));
        assertEquals("to String", "<BitRow>11111110</BitRow>", lBits1.toString());
    }
}
