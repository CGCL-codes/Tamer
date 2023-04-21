package jmathlibtests.toolbox.jmathlib.internal;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;

public class testLn extends TestCase {

    protected Interpreter ml;

    public testLn(String name) {
        super(name);
    }

    public static void main(String[] args) {
        jmathlib.tools.junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        ml = new Interpreter(true);
    }

    protected void tearDown() {
        ml = null;
    }

    public static Test suite() {
        return new TestSuite(testLn.class);
    }

    public void testAbs01() {
        ml.executeExpression("a=abs(1);");
        assertTrue(1 == ml.getScalarValueRe("a"));
    }

    public void testAbs02() {
        ml.executeExpression("a=abs(-3);");
        assertTrue(3 == ml.getScalarValueRe("a"));
    }

    public void testLn03() {
        ml.throwErrorsB = true;
        try {
            ml.executeExpression("m=ln(4,5) ");
        } catch (Exception e) {
            assertTrue(true);
            return;
        }
        assertTrue(false);
    }
}
