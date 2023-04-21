package jmathlibtests.toolbox.jmathlib.matrix;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;
import jmathlibtests.Compare;

public class testSumsq extends TestCase {

    protected Interpreter ml;

    public testSumsq(String name) {
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
        return new TestSuite(testSumsq.class);
    }

    public void testSumsq01() {
        ml.executeExpression("a=sumsq([1,2,3])");
        assertTrue(14.0 == ml.getScalarValueRe("a"));
    }

    public void testSumsq02() {
        ml.executeExpression("a=sumsq([2,2,3])");
        assertTrue(17.0 == ml.getScalarValueRe("a"));
    }
}
