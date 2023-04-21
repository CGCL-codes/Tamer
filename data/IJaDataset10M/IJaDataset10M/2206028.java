package jmathlibtests.toolbox.jmathlib.matrix;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;
import jmathlibtests.Compare;

public class testLe extends TestCase {

    protected Interpreter ml;

    public testLe(String name) {
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
        return new TestSuite(testLe.class);
    }

    /*****************************************************************/
    public void testLe01() {
        ml.executeExpression("a=le(5,5);");
        assertTrue(true == ml.getScalarValueBoolean("a"));
    }

    public void testLe02() {
        ml.executeExpression("b=le(5,6)");
        assertTrue(true == ml.getScalarValueBoolean("b"));
    }

    public void testLe03() {
        ml.executeExpression("b=le(6,5)");
        assertTrue(false == ml.getScalarValueBoolean("b"));
    }

    public void testLe04() {
        ml.executeExpression("b=le(0,0)");
        assertTrue(true == ml.getScalarValueBoolean("b"));
    }

    public void testLe05() {
        boolean[][] c = { { false, true }, { true, true } };
        ml.executeExpression("a=[1,3; 2,3]");
        ml.executeExpression("b=[0,7; 2,7]");
        ml.executeExpression("z = le(a,b)");
        assertTrue(Compare.ArrayEquals(c, ml.getArrayValueBoolean("z")));
    }
}
