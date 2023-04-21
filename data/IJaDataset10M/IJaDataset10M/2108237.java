package com.rubecula.jexpression.eval;

import com.rubecula.jexpression.EvalExpressionMutable;
import com.rubecula.jexpression.Evaluator;
import com.rubecula.jexpression.JexpressionException;

/**
 * TODO consider eliminating this class.
 * 
 * This is the appropriate class to use when utilizing the "Eval" package with
 * the need to vary the expression.
 * 
 * @author Robin Hillyard
 * 
 */
public class Evaluator_Eval_Direct_Mutable extends Evaluator_Eval_Direct implements EvalExpressionMutable {

    /**
	 * @throws JexpressionException
	 * 
	 */
    public Evaluator_Eval_Direct_Mutable() throws JexpressionException {
        super();
    }

    /**
	 * @return true
	 * @see com.rubecula.jexpression.EvalExpressionMutable#isMutable()
	 */
    public boolean isMutable() {
        return true;
    }

    /**
	 * Updates the value of the expression for this {@link Evaluator}.
	 * 
	 * @see com.rubecula.jexpression.EvalExpressionMutable#setExpression(java.lang.String)
	 */
    public void setExpression(final String expression) {
        this.expression = expression;
        this.evaluator = new net.java.dev.eval.Expression(expression);
    }

    /**
	 * @see com.rubecula.jexpression.EvalExpressionMutable#setExpression(java.lang.CharSequence[])
	 */
    public void setExpression(CharSequence... tokens) throws JexpressionException {
        setExpression(formExpression(this, tokens));
    }
}
