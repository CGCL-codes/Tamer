package org.objectstyle.cayenne.exp;

import org.objectstyle.cayenne.unit.CayenneTestCase;

/**
 * @deprecated since 1.2
 */
public class UnaryExpressionTst extends CayenneTestCase {

    private static final int defaultType = -34;

    protected UnaryExpression expr;

    protected void setUp() throws java.lang.Exception {
        expr = new UnaryExpression(defaultType);
    }

    public void testGetType() throws java.lang.Exception {
        assertEquals(defaultType, expr.getType());
    }

    public void testGetOperandCount() throws java.lang.Exception {
        assertEquals(1, expr.getOperandCount());
    }

    public void testGetOperandAtIndex() throws java.lang.Exception {
        expr.getOperand(0);
        try {
            expr.getOperand(1);
            fail();
        } catch (Exception ex) {
        }
    }

    public void testSetOperandAtIndex() throws java.lang.Exception {
        Object obj = new Object();
        expr.setOperand(0, obj);
        assertSame(obj, expr.getOperand(0));
        try {
            expr.setOperand(1, obj);
            fail();
        } catch (Exception ex) {
        }
    }
}
