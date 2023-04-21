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

/** Unit tests for th sign expression.
 *
 * @since 0.9.1
 */
public class ExprSIGNTest {

    public ExprSIGNTest() {
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
     * Test of eval method, of class ExprSIGN.
     */
    @Test
    public void testEval() {
        System.out.println("ExprSIGN");
        try {
            EquationParser.acceptUndefinedVariables(true);
            Equation condition = EquationParser.parse("sign(a)");
            Map<String, Variable> vars = condition.getVariables();
            assertEquals("Must have 1 variable", 1, vars.size());
            condition.getVariable("a").setValue(3);
            Object result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            assertEquals("Result must be 1", 1, ((Integer) result).intValue());
            condition.getVariable("a").setValue(-2);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            assertEquals("Result must be -1", -1, ((Integer) result).intValue());
            condition.getVariable("a").setValue(0);
            result = condition.eval();
            assertTrue("Result must be Integer", result instanceof Integer);
            assertEquals("Result must be 0", 0, ((Integer) result).intValue());
        } catch (ParseException ex) {
            ex.printStackTrace();
            fail("Fail to parse");
        }
    }
}
