package com.rubecula.jexpression.eval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import org.junit.Test;

/**
* @author Robin Hillyard
 * 
 */
public class Evaluator_Eval_DirectTest {

    private static final String E_X_ILLEGAL = "e^x";

    private static final String E_POW_X = "e pow x";

    private static final String X_2_Y_1 = "x+2*(y+1)";

    /**
	 * @throws Exception
	 */
    @SuppressWarnings("nls")
    @Test
    public void testEvaluator_EvalDouble() throws Exception {
        Evaluator_Eval_Direct_Mutable eval = new Evaluator_Eval_Direct_Mutable();
        eval.setExpression(X_2_Y_1);
        eval.addSymbolPrimitive("x", 3.0);
        eval.addSymbolPrimitive("y", 2.0);
        double value = eval.getValuePrimitive();
        assertEquals("eval", 9, value, 0.0000001);
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings("nls")
    @Test
    public void testEvaluator_EvalObject() throws Exception {
        Evaluator_Eval_Direct_Mutable eval = new Evaluator_Eval_Direct_Mutable();
        eval.setExpression(X_2_Y_1);
        eval.addSymbol("x", BigDecimal.valueOf(3));
        eval.addSymbol("y", BigDecimal.valueOf(2));
        Object value = eval.getValue();
        assertEquals("eval", BigDecimal.valueOf(9), value);
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings("nls")
    @Test
    public void testEvaluator_EvalDouble2() throws Exception {
        Evaluator_Eval_Direct_Mutable eval = new Evaluator_Eval_Direct_Mutable();
        eval.setExpression(E_POW_X);
        assertEquals(E_POW_X, eval.getExpression());
        eval.addSymbol("e", BigDecimal.valueOf(Math.E));
        eval.addSymbol("x", BigDecimal.valueOf(1));
        assertEquals("e^1", Math.E, eval.getValuePrimitive(), 0.0000001);
        eval.removeSymbol("x");
        eval.addSymbol("x", BigDecimal.valueOf(2));
        assertEquals("e^2", Math.E * Math.E, eval.getValuePrimitive(), 0.0000001);
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings("nls")
    @Test
    public void testEvaluator_Eval2() throws Exception {
        Evaluator_Eval_Direct_Mutable eval = new Evaluator_Eval_Direct_Mutable();
        eval.setExpression(X_2_Y_1);
        eval.addSymbolPrimitive("x", 3.0);
        eval.addSymbolPrimitive("y", 2.0);
        assertEquals("eval", 9, eval.getValuePrimitive(), 0.0000001);
        assertEquals("eval", 9, eval.getValuePrimitive(), 0.0000001);
    }

    /**
	 * This one no longer fails.
	 */
    @SuppressWarnings("nls")
    public void testGetErrorInfo() {
        try {
            Evaluator_Eval_Direct_Mutable eval = new Evaluator_Eval_Direct_Mutable();
            eval.setExpression(E_X_ILLEGAL);
            fail("bad expression");
            assertEquals("", eval.getErrorInfo());
        } catch (Exception e) {
        }
    }
}
