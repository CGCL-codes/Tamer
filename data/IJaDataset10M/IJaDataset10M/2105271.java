package com.w20e.socrates.expression;

import com.w20e.socrates.expression.GTE;
import com.w20e.socrates.expression.XNumber;
import junit.framework.TestCase;

public class TestGTE extends TestCase {

    private XNumber num0;

    private XNumber num1;

    private GTE op;

    public TestGTE(String name) {
        super(name);
    }

    public void setUp() {
        this.num0 = new XNumber(new Integer(10));
        this.num1 = new XNumber(new Integer(5));
        this.op = new GTE();
        this.op.setLeftOperand(this.num0);
        this.op.setRightOperand(this.num1);
    }

    public void testGetType() {
        assertEquals("GTE", this.op.getType());
    }

    public void testToBoolean() {
        assertTrue(this.op.toBoolean());
        this.op.setRightOperand(new XNumber(new Integer(10)));
        assertTrue(this.op.toBoolean());
        this.op.setRightOperand(new XNumber(new Integer(11)));
        assertFalse(this.op.toBoolean());
    }

    public void testToString() {
        assertEquals(this.op.toString(), "10 >= 5");
    }

    public void testEval() {
        assertTrue(this.op.eval().toBoolean());
        this.op.setRightOperand(new XNumber(new Integer(11)));
        assertFalse(this.op.eval().toBoolean());
        this.op.setLeftOperand(null);
        this.op.setRightOperand(new XNumber(new Integer(-11)));
        assertFalse(this.op.eval().toBoolean());
        this.op.setRightOperand(null);
        assertTrue(this.op.eval().toBoolean());
        this.op.setLeftOperand(new XNumber(new Integer(10)));
        assertTrue(this.op.eval().toBoolean());
    }
}
