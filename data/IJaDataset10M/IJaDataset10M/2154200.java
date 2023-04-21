package test;

import junit.framework.*;
import iwork.patchpanel.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TestSuiteConversion extends TestCase {

    protected static final boolean VERBOSE = true;

    protected MyEvaluator evaluator;

    protected void setUp() {
        evaluator = new MyEvaluator();
    }

    /*****Test number conversion****/
    public void testIntToInt() {
        String exp = "=(int)1";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Integer);
        assertTrue(((Integer) res).intValue() == 1);
    }

    public void testIntToTrue() {
        String exp = "=(boolean)1";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Boolean);
        assertTrue(((Boolean) res).booleanValue() == true);
    }

    public void testIntToFalse() {
        String exp = "=(boolean)0";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Boolean);
        assertTrue(((Boolean) res).booleanValue() == false);
    }

    public void testIntToString() {
        String exp = "=(string)1";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof String);
        assertTrue(((String) res).equalsIgnoreCase("1"));
    }

    public void testIntToOverMaxByte() {
        String exp = "=(byte)256";
        Object res = evaluate(exp);
        assertNotNull(res);
    }

    public void testIntToMaxByte() {
        String exp = "=(byte)255";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Byte);
        assertTrue(((Byte) res).byteValue() == (byte) 255);
    }

    public void testIntToMinByte() {
        String exp = "=(byte)0";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Byte);
        assertTrue(((Byte) res).byteValue() == (byte) 0);
    }

    public void testIntToDouble() {
        String exp = "=(double)100000";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Double);
        assertTrue(((Double) res).doubleValue() == 100000.0);
    }

    public void testIntToFloat() {
        String exp = "=(float)100000";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Float);
        assertTrue(((Float) res).floatValue() == 100000.0f);
    }

    public void testIntToLong() {
        String exp = "=(long)100000";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Long);
        assertTrue(((Long) res).floatValue() == 100000.0f);
    }

    public void testIntToShort() {
        String exp = "=(short)100000";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Short);
        assertTrue(((Short) res).floatValue() == 100000.0f);
    }

    public void testTrueToString() {
        String exp = "=(string)TRUE";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof String);
        assertTrue(((String) res).equalsIgnoreCase("true"));
    }

    public void testFalseToString() {
        String exp = "=(string)FALSE";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof String);
        assertTrue(((String) res).equalsIgnoreCase("false"));
    }

    public void testTrueToFloat() {
        String exp = "=(float)TRUE";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Float);
        assertTrue(((Float) res).floatValue() == 1.0f);
    }

    public void testFalseToFloat() {
        String exp = "=(float)FALSE";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Float);
        assertTrue(((Float) res).floatValue() == 0.0f);
    }

    public void testStringToBool() {
        String exp = "=(boolean)\"T\"";
        Object res = evaluate(exp);
        assertNotNull(res);
        assertTrue(res instanceof Boolean);
        assertTrue(((Boolean) res).booleanValue() == true);
    }

    public void testVartoInt() {
        VariableResolver vr = new VariableResolver() {

            public Object resolve(String s, int callCount) {
                if (s.equalsIgnoreCase("x")) return new Float(3.14f); else return null;
            }
        };
        evaluator.registerVariableResolver(vr);
        String exp = "=(int)x";
        Object res = evaluate(exp);
        if (VERBOSE) System.out.println(" " + (Integer) res);
        assertNotNull(res);
        assertTrue(res instanceof Integer);
        assertTrue(((Integer) res).intValue() == (byte) 3);
    }

    protected Object evaluate(String expression) {
        Object res = null;
        try {
            if (VERBOSE) System.out.print(expression);
            evaluator.setExpression(expression);
            res = evaluator.evaluate(20);
        } catch (iwork.patchpanel.MyEvaluator.EvaluatorException exp) {
            if (VERBOSE) {
                System.out.println(exp.getMessage());
                exp.printStackTrace();
            }
        }
        System.out.println("\t.....Result: " + res);
        return res;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TestSuiteExpression.class);
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
