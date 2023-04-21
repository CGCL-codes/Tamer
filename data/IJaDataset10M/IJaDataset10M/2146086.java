package org.norecess.citkit.tir.expressions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.norecess.citkit.tir.factories.HobbesTIRFactory.makeSeqExp;
import java.util.Iterator;
import org.junit.Test;
import org.norecess.citkit.tir.ExpressionTIR;
import org.norecess.citkit.tir.HobbesTIR;
import org.norecess.citkit.tir.expressions.IntegerETIR;

public class SequenceETIRTest {

    @Test
    public void testEquals() {
        assertEquals(makeSeqExp(1, 2), makeSeqExp(1, 2));
        assertNotEquals(makeSeqExp(1, 2), makeSeqExp(9, 2));
        assertNotEquals(makeSeqExp(1, 2), makeSeqExp(1, 9));
    }

    @Test
    public void testHashCode() {
        assertEquals(makeSeqExp(1, 2).hashCode(), makeSeqExp(1, 2).hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("[1, 2]", makeSeqExp(1, 2).toString());
        assertEquals("[9, 8]", makeSeqExp(9, 8).toString());
    }

    @Test
    public void shouldIterate() {
        Iterator<ExpressionTIR> iterator = makeSeqExp(6, 888).iterator();
        assertEquals(new IntegerETIR(6), iterator.next());
        assertEquals(new IntegerETIR(888), iterator.next());
        assertFalse(iterator.hasNext());
    }

    protected void assertNotEquals(HobbesTIR first, HobbesTIR second) {
        assertFalse(first.equals(second));
    }
}
