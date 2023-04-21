package jmathlibtests.core.tokens;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;

public class testDelimiterToken extends TestCase {

    protected Interpreter ml;

    public testDelimiterToken(String name) {
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
        return new TestSuite(testDelimiterToken.class);
    }

    /************* simple expressions ****************************************/
    public void test001() {
        ml.executeExpression("a=2+3");
        assertTrue(5 == ml.getScalarValueRe("a"));
    }
}
