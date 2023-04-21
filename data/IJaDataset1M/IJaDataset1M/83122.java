package org.apache.velocity.test;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MathException;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * This class tests support for strict math mode.
 */
public class StrictMathTestCase extends BaseEvalTestCase {

    public StrictMathTestCase(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        engine.setProperty(RuntimeConstants.STRICT_MATH, Boolean.TRUE);
        context.put("num", new Integer(5));
        context.put("zero", new Integer(0));
    }

    protected void assertNullMathEx(String operation) {
        String leftnull = "#set( $foo = $null " + operation + " $num )";
        assertEvalException(leftnull, MathException.class);
        String rightnull = "#set( $foo = $num " + operation + " $null )";
        assertEvalException(rightnull, MathException.class);
    }

    protected void assertImaginaryMathEx(String operation) {
        String infinity = "#set( $foo = $num " + operation + " $zero )";
        assertEvalException(infinity, MathException.class);
    }

    public void testAdd() {
        assertNullMathEx("+");
    }

    public void testSub() {
        assertNullMathEx("-");
    }

    public void testMul() {
        assertNullMathEx("*");
    }

    public void testMod() {
        assertNullMathEx("%");
        assertImaginaryMathEx("%");
    }

    public void testDiv() {
        assertNullMathEx("/");
        assertImaginaryMathEx("/");
    }
}
