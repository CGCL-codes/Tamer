package jmathlibtests.toolbox.general;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;

public class testImag extends TestCase {

    protected Interpreter ml;

    public testImag(String name) {
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
        return new TestSuite(testImag.class);
    }

    public void testImag01() {
        ml.executeExpression("a=5+6i");
        ml.executeExpression("b=imag(a)");
        assertTrue(6 == ml.getScalarValueRe("b"));
    }

    public void testImag02() {
        ml.executeExpression("a=55");
        ml.executeExpression("b=imag(a)");
        assertTrue(0 == ml.getScalarValueRe("b"));
    }

    public void testImag03() {
        ml.executeExpression("a=56i");
        ml.executeExpression("b=imag(a)");
        assertTrue(56 == ml.getScalarValueRe("b"));
    }
}
