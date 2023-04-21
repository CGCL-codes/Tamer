package org.da.expressionj.expr;

import java.util.Map;
import org.da.expressionj.expr.parser.EquationParser;
import org.da.expressionj.expr.parser.ParseException;
import org.da.expressionj.model.Equation;
import org.da.expressionj.model.Variable;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit tests for the CHOICE expression.
 *
 * @version 0.9.2
 */
public class ExprCHOICETest {

    public ExprCHOICETest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
    * Test of parse method, of class ExprCHOICE.
    */
    @Test
    public void testParseSimple() {
        System.out.println("parseSimple");
        try {
            Equation condition = EquationParser.parse("if (c) {return 2} else {return 3};");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 1 variable", 1, vars.size());
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of parse method, of class ExprCHOICE.
    */
    @Test
    public void testParseSimpleWithoutComma() {
        System.out.println("parseSimpleWithoutComma");
        try {
            Equation condition = EquationParser.parse("if (c) {return 2} else {return 3}");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 1 variable", 1, vars.size());
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of eval method, of class ExprCHOICE.
    */
    @Test
    public void testEvalSimple() {
        System.out.println("evalSimple");
        try {
            Equation condition = EquationParser.parse("if (c) {return 2} else {return 3};");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 1 variable", 1, vars.size());
            condition.getVariable("c").setValue(true);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            int value = ((Integer) result).intValue();
            assertEquals("Result must be 2", 2, value);
            condition.getVariable("c").setValue(false);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 3", 3, value);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of eval method, of class ExprCHOICE.
    */
    @Test
    public void testEvalWithElseIf() {
        System.out.println("evalWithElseif");
        try {
            Equation condition = EquationParser.parse("if (c) {return 2} else if (b == 2) {return 4} else {return 3};");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 2 variable1", 2, vars.size());
            condition.getVariable("c").setValue(true);
            condition.getVariable("b").setValue(1);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            int value = ((Integer) result).intValue();
            assertEquals("Result must be 2", 2, value);
            condition.getVariable("c").setValue(false);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 3", 3, value);
            condition.getVariable("b").setValue(2);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 4", 4, value);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of eval method, of class ExprCHOICE.
    */
    @Test
    public void testEvalWithElseIf2() {
        System.out.println("evalWithElseif2");
        try {
            Equation condition = EquationParser.parse("if (!c) {return 2} else if (b == 2) {return b * 10} else {return 3};");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 2 variable1", 2, vars.size());
            condition.getVariable("c").setValue(false);
            condition.getVariable("b").setValue(1);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            int value = ((Integer) result).intValue();
            assertEquals("Result must be 2", 2, value);
            condition.getVariable("c").setValue(true);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 3", 3, value);
            condition.getVariable("b").setValue(2);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 20", 20, value);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of eval method, of class ExprCHOICE.
    */
    @Test
    public void testEvalWithNewLines() {
        System.out.println("evalWithNewLines");
        try {
            String newLine = System.getProperty("line.separator");
            StringBuilder buf = new StringBuilder();
            buf.append("if (!c) {return 2}").append(newLine);
            buf.append("else if (b == 2) {return b * 10}").append(newLine);
            buf.append("else {return 3};");
            Equation condition = EquationParser.parse(buf.toString());
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 2 variable1", 2, vars.size());
            condition.getVariable("c").setValue(false);
            condition.getVariable("b").setValue(1);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            int value = ((Integer) result).intValue();
            assertEquals("Result must be 2", 2, value);
            condition.getVariable("c").setValue(true);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 3", 3, value);
            condition.getVariable("b").setValue(2);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 20", 20, value);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of eval method, of class ExprCHOICE.
    */
    @Test
    public void testEvalWithSubExpressions() {
        System.out.println("evalWithSubExpressions");
        try {
            String newLine = System.getProperty("line.separator");
            StringBuilder buf = new StringBuilder();
            buf.append("if (!c) {return 2}").append(newLine);
            buf.append("else if (b == 2) {").append(newLine);
            buf.append(" if (d == 3) { return 25} else {return 45}").append(newLine);
            buf.append("} else {return 3};");
            Equation condition = EquationParser.parse(buf.toString());
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 3 variable1", 3, vars.size());
            condition.getVariable("c").setValue(false);
            condition.getVariable("b").setValue(1);
            condition.getVariable("d").setValue(4);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            int value = ((Integer) result).intValue();
            assertEquals("Result must be 2", 2, value);
            condition.getVariable("c").setValue(true);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 3", 3, value);
            condition.getVariable("b").setValue(2);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 45", 45, value);
            condition.getVariable("d").setValue(3);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            value = ((Integer) result).intValue();
            assertEquals("Result must be 25", 25, value);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
    * Test of an if-then-else without else.
    */
    @Test
    public void testEvalWithoutElse() {
        System.out.println("evalWithoutElse");
        try {
            String newLine = System.getProperty("line.separator");
            StringBuilder buf = new StringBuilder();
            buf.append("if (e == 5) {").append(newLine);
            buf.append("  e = 2").append(newLine);
            buf.append("};");
            buf.append("return e;");
            Equation condition = EquationParser.parse(buf.toString());
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 1 variable", 1, vars.size());
            condition.getVariable("e").setValue(5);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            int value = ((Integer) result).intValue();
            assertEquals("Result must be 2", 2, value);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }

    /**
     * Test of eval with echo.
     */
    @Test
    public void testEvalWithEcho() {
        System.out.println("testEvalCHOICEWithEcho");
        try {
            Equation condition = EquationParser.parse("if (a) { echo(\"first\"); 2} else { echo(\"second\"); 3}");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 1 variable", 1, vars.size());
            condition.getVariable("a").setValue(false);
            Object o = condition.eval();
            assertTrue(o instanceof Integer);
            int res = ((Integer) o).intValue();
            assertEquals("Return value must be 3", 3, res);
            condition.getVariable("a").setValue(true);
            o = condition.eval();
            assertTrue(o instanceof Integer);
            res = ((Integer) o).intValue();
            assertEquals("Return value must be 2", 2, res);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            fail("Fail to parse");
        }
    }
}
