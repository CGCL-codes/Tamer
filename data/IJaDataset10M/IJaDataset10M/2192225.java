package org.norecess.citkit.tir.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.norecess.citkit.tir.factories.HobbesTIRFactory.makeCallExp;
import org.junit.Test;
import org.norecess.citkit.tir.HobbesTIR;

public class CallETIRTest {

    @Test
    public void testEquals() {
        assertEquals(makeCallExp(1, 2), makeCallExp(1, 2));
        assertNotEquals(makeCallExp(1, 2), makeCallExp(1, 9));
        assertNotEquals(makeCallExp(1, 2), makeCallExp(9, 2));
    }

    @Test
    public void testHashCode() {
        assertEquals(makeCallExp(1, 2).hashCode(), makeCallExp(1, 2).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("func1[2, 2]", makeCallExp(1, 2).toString());
        assertEquals("func9[8, 8]", makeCallExp(9, 8).toString());
    }

    @Test
    public void testType() {
        assertNull(makeCallExp(1, 2).getType());
    }

    protected void assertNotEquals(HobbesTIR first, HobbesTIR second) {
        assertFalse(first.equals(second));
    }
}
