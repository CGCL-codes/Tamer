package jmathlib.tools.junit.framework;

import jmathlib.core.interpreter.Interpreter;
import jmathlibtests.Compare;

public class JMathLibTestCase extends TestCase {

    protected Interpreter ml;

    public JMathLibTestCase(String name) {
        super(name);
    }

    protected void setUp() {
        ml = new Interpreter(true);
    }

    protected void tearDown() {
        ml = null;
    }

    public void eval(String s) {
        ml.executeExpression(s);
    }

    public void assertStringEquals(String a, String b) {
        ml.executeExpression(a);
        double[][] ans1D = ml.getArrayValueRe("ans");
        double[][] ans1I = ml.getArrayValueIm("ans");
        ml.executeExpression(b);
        double[][] ans2D = ml.getArrayValueRe("ans");
        double[][] ans2I = ml.getArrayValueIm("ans");
        assertTrue(Compare.ArrayEquals(ans1D, ans2D));
        assertTrue(Compare.ArrayEquals(ans1I, ans2I));
    }

    public void assertEvalScalarEquals(String func, String var, boolean b) {
        ml.executeExpression(func);
        boolean ansRe = ml.getScalarValueBoolean(var);
        assertEquals(ansRe, b);
    }

    public void assertScalarEquals(String var, double varRe) {
        double ansRe = ml.getScalarValueRe(var);
        assertEquals(ansRe, varRe);
    }

    public void assertScalarEquals(String var, double varRe, double tol) {
        double ansRe = ml.getScalarValueRe(var);
        assertEquals(ansRe, varRe, tol);
    }

    public void assertScalarEquals(String var, double varRe, double varIm, double tol) {
        double ansRe = ml.getScalarValueRe(var);
        double ansIm = ml.getScalarValueIm(var);
        assertEquals(ansRe, varRe, tol);
        assertEquals(ansIm, varIm, tol);
    }

    public void assertEvalScalarEquals(String func, String var, double varRe) {
        ml.executeExpression(func);
        double ansRe = ml.getScalarValueRe(var);
        assertEquals(ansRe, varRe);
    }

    public void assertEvalScalarEquals(String func, String var, double varRe, double tol) {
        ml.executeExpression(func);
        double ansRe = ml.getScalarValueRe(var);
        assertEquals(ansRe, varRe, tol);
    }

    public void assertEvalScalarEquals(String func, String var, double varRe, double varIm, double tol) {
        ml.executeExpression(func);
        double ansRe = ml.getScalarValueRe(var);
        double ansIm = ml.getScalarValueIm(var);
        assertEquals(ansRe, varRe, tol);
        assertEquals(ansIm, varIm, tol);
    }

    public void assertEvalArrayEquals(String func, String var, double[][] varRe) {
        ml.executeExpression(func);
        double[][] ansRe = ml.getArrayValueRe(var);
        assertTrue(Compare.ArrayEquals(ansRe, varRe));
    }

    public void assertEvalArrayEquals(String func, String var, double[][] varRe, double tol) {
        ml.executeExpression(func);
        double[][] ansRe = ml.getArrayValueRe(var);
        assertTrue(Compare.ArrayEquals(ansRe, varRe, tol));
    }

    public void assertEvalArrayEquals(String func, String var, double[][] varRe, double[][] varIm, double tol) {
        ml.executeExpression(func);
        double[][] ansRe = ml.getArrayValueRe(var);
        double[][] ansIm = ml.getArrayValueIm(var);
        assertTrue(Compare.ArrayEquals(ansRe, varRe, tol));
        assertTrue(Compare.ArrayEquals(ansIm, varIm, tol));
    }
}
