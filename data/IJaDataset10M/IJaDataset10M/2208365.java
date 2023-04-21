package org.norecess.citkit.tir.declarations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.norecess.citkit.tir.factories.HobbesTIRFactory.makeIntExp;
import static org.norecess.citkit.tir.factories.HobbesTIRFactory.makeVarDec;
import org.junit.Test;
import org.norecess.citkit.Symbol;
import org.norecess.citkit.tir.HobbesTIR;

public class VariableDeclarationTIRTest {

    @Test
    public void testEquals() {
        assertEquals(makeVarDec(1, 3), makeVarDec(1, 3));
        assertEquals(makeVarDec(1, 2, 3), makeVarDec(1, 2, 3));
        assertNotEquals(makeVarDec(1, 2, 3), makeVarDec(9, 2, 3));
        assertNotEquals(makeVarDec(1, 2, 3), makeVarDec(1, 9, 3));
        assertNotEquals(makeVarDec(1, 2, 3), makeVarDec(1, 2, 9));
        assertNotEquals(makeVarDec(1, 2), null);
        assertNotEquals(makeVarDec(1, 3), makeVarDec(1, 2, 3));
        assertNotEquals(makeVarDec(1, 2, 3), makeVarDec(1, 3));
    }

    @Test
    public void testHashCode() {
        assertEquals(makeVarDec(1, 3).hashCode(), makeVarDec(1, 3).hashCode());
        assertEquals(makeVarDec(1, 2, 3).hashCode(), makeVarDec(1, 2, 3).hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldRefuseNullName() {
        new VariableDTIR(null, makeIntExp(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorShouldRefuseNullExpression() {
        new VariableDTIR(Symbol.createSymbol("var1"), null);
    }

    @Test
    public void testToString() {
        assertEquals("var1 : nametype2 := 3", makeVarDec(1, 2, 3).toString());
    }

    protected void assertNotEquals(HobbesTIR first, HobbesTIR second) {
        assertFalse(first.equals(second));
    }
}
