package jmathlibtests.core.tokens;

import jmathlib.core.interpreter.Interpreter;
import jmathlib.tools.junit.framework.*;

public class testSwitchOperatorToken extends TestCase {

    protected Interpreter ml;

    public testSwitchOperatorToken(String name) {
        super(name);
    }

    public static void main(String[] args) {
        jmathlib.tools.junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(testSwitchOperatorToken.class);
    }

    public void setUp() {
        ml = new Interpreter(true);
    }

    protected void tearDown() {
        ml = null;
    }

    public void testSwitch01() {
        String code = "" + " switch(x) \n" + "case(1) { t=11; } " + "case(2) { t=22; } " + "end; ";
        ml.executeExpression(" x=1;" + code);
        assertTrue(ml.getScalarValueRe("t") == 11);
    }

    public void testSwitch02() {
        String code = "" + " switch(x) \n" + "case(1) { t=11; } " + "case(2) { t=22; } " + "end; ";
        ml.executeExpression(" x=2;" + code);
        assertTrue(ml.getScalarValueRe("t") == 22);
    }

    public void testSwitch03a() {
        String code = "" + " switch(x) \n" + "case(1) { t=11; } " + "case(2) { t=22; } " + "default { t=33; } " + "end; ";
        ml.executeExpression(" x=4;" + code);
        assertTrue(ml.getScalarValueRe("t") == 33);
    }

    public void testSwitch03b() {
        String code = "" + " switch(x) \n" + "case(1) { t=11; } " + "case(2) { t=2266; } " + "default { t=33; } " + "end; ";
        ml.executeExpression(" x=2;" + code);
        assertTrue(ml.getScalarValueRe("t") == 2266);
    }

    public void testSwitch04a() {
        ml.executeExpression(" x=1; test_switch001;");
        assertTrue(ml.getScalarValueRe("y") == 123.0);
    }

    public void testSwitch04b() {
        ml.executeExpression(" x=2; test_switch001;");
        assertTrue(ml.getScalarValueRe("y") == 234.0);
    }

    public void testSwitch04c() {
        ml.executeExpression(" x=3; test_switch001;");
        assertTrue(ml.getScalarValueRe("y") == 345.0);
    }

    public void testSwitch04d() {
        ml.executeExpression(" x=333; test_switch001;");
        assertTrue(ml.getScalarValueRe("y") == 777);
    }

    public void testSwitch05a() {
        String code = "" + " switch(x) \n" + "case(1)  t=11; b=55; c=sin(6); " + "case(2) { t=22; } " + "default { t=33; } " + "end; ";
        ml.executeExpression(" x=1;" + code);
        assertTrue(ml.getScalarValueRe("t") == 11);
    }

    public void testSwitch05b() {
        String code = "" + " switch(x) \n" + "case(2) { t=22; } " + "case(13)  t=11; b=55; c=sin(6); " + "default { t=33; } " + "end; ";
        ml.executeExpression(" x=13;" + code);
        assertTrue(ml.getScalarValueRe("t") == 11);
    }

    public void testSwitch05c() {
        String code = "" + " switch(x)\n " + "case(2) { t=22; } " + "case(13)  b=55; c=sin(6); t=11; " + "default { t=33; } " + "end; ";
        ml.executeExpression(" x=13;" + code);
        assertTrue(ml.getScalarValueRe("t") == 11);
    }

    public void testSwitch06a() {
        String code = "" + " switch(x) \n" + "case(1) t=11;  " + "case(2)  t=22;  " + "case(3)  t=22;  " + "case(6)  t=22;  " + "case(33.6)  t=22;  " + "case(987)  t=22;  " + "otherwise  t=3345;  " + "endswitch; ";
        ml.executeExpression(" x=4;" + code);
        assertTrue(ml.getScalarValueRe("t") == 3345);
    }

    public void testSwitch06b() {
        String code = "" + " switch( x) \n " + "case(1) t=11;  " + "case(2)  t=22;  " + "case(3)  t=22;  " + "case(6)  t=22;  " + "case(33.6)  t=22;  " + "case(987)  t=22;  " + "otherwise  t=334;  " + "endswitch; ";
        ml.executeExpression(" x=987;" + code);
        assertTrue(ml.getScalarValueRe("t") == 22);
    }

    public void testSwitch07a() {
        String code = "" + " switch (x) \n" + "case(1)  t=11;  " + "case(22)  t=22;  " + "otherwise  t=3398;  " + "endswitch; ";
        ml.executeExpression(" x=22;" + code);
        assertTrue(ml.getScalarValueRe("t") == 22);
    }

    public void testSwitch07b() {
        String code = "" + " switch (x) \n" + "case(-1)  t=111;  " + "case(22)  t=22;  " + "otherwise  t=3398;  " + "endswitch; ";
        ml.executeExpression(" x=-1;" + code);
        assertTrue(ml.getScalarValueRe("t") == 111);
    }

    public void testSwitch07c() {
        String code = "" + " switch (x) \n" + "case(-1)  t=111;  " + "case(22)  t=22;  " + "otherwise  t=3398;  " + "endswitch; ";
        ml.executeExpression(" x=111;" + code);
        assertTrue(ml.getScalarValueRe("t") == 3398);
    }

    public void testSwitch07d() {
        String code = "" + " switch (x) \n" + "case(-1) y=55; t=111;  " + "case(22)  t=22;  " + "default  t=3398;  " + "endswitch; ";
        ml.executeExpression(" x=-1;" + code);
        assertTrue(ml.getScalarValueRe("t") == 111);
    }

    public void testSwitch08() {
        String code = "" + "cc=789; \n" + " switch (x) \n" + "case(-1) y=55; t=111;  " + "case(232)  t=22;  " + "default  t=3398;  " + "endswitch; " + "ccc=7899;";
        ml.executeExpression(" x=232;" + code);
        assertTrue(ml.getScalarValueRe("t") == 22);
        assertTrue(ml.getScalarValueRe("cc") == 789);
        assertTrue(ml.getScalarValueRe("ccc") == 7899);
    }
}
