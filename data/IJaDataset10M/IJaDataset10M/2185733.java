package jmathlibtests.toolbox.jmathlib.matrix;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;

public class testIsreal extends TestCase {

    protected Interpreter ml;

    public testIsreal(String name) {
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
        return new TestSuite(testIsreal.class);
    }

    public void testIsreal01() {
        ml.executeExpression("a=isreal(12);");
        assertTrue(1 == ml.getScalarValueRe("a"));
    }

    public void testIsreal02() {
        ml.executeExpression("a=isreal([]);");
        assertTrue(1 == ml.getScalarValueRe("a"));
    }

    public void testIsreal03() {
        ml.executeExpression("a=isreal(3i);");
        assertTrue(0 == ml.getScalarValueRe("a"));
    }

    public void testIsreal04() {
        ml.executeExpression("a=isreal(5+4j);");
        assertTrue(0 == ml.getScalarValueRe("a"));
    }

    public void testIsreal05() {
        ml.executeExpression("a=isreal([1,2,3,4]);");
        assertTrue(1 == ml.getScalarValueRe("a"));
    }

    public void testIsreal06() {
        ml.executeExpression("a=isreal([1,2j;3,4]);");
        assertTrue(0 == ml.getScalarValueRe("a"));
    }
}
