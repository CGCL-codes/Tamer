package com.rubecula.jexpression.eval;

import java.util.ArrayList;
import java.util.Collection;
import com.rubecula.jexpression.Concept;
import com.rubecula.jexpression.Evaluator;
import com.rubecula.jexpression.EvaluatorMutable;
import com.rubecula.jexpression.JexpressionException;
import com.rubecula.jexpression.Listener;
import com.rubecula.jexpression.Notation;
import com.rubecula.jexpression.Term;

/**
 * Mutable implementation of Evaluator for the "eval" package. This class
 * delegates to Evaluator and creates a new evaluator when the expression
 * changes.
 * 
 * @author Robin Hillyard
 * 
 */
public class Evaluator_Eval_Mutable implements EvaluatorMutable {

    /**
	 * @param tokens
	 * @throws JexpressionException
	 */
    public Evaluator_Eval_Mutable(final CharSequence... tokens) throws JexpressionException {
        setExpression(tokens);
    }

    /**
	 * @param listener
	 * @see com.rubecula.jexpression.EvaluatorMutable#addListener(com.rubecula.jexpression.Listener)
	 */
    public void addListener(final Listener listener) {
        _listeners.add(listener);
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#addSymbol(java.lang.String,
	 *      java.lang.Number)
	 */
    public Number addSymbol(final String name, final Number value) throws JexpressionException {
        return evaluator.addSymbol(name, value);
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#getErrorInfo()
	 */
    public Object getErrorInfo() throws JexpressionException {
        return evaluator.getErrorInfo();
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#getExpression()
	 */
    public String getExpression() {
        return evaluator.getExpression();
    }

    /**
	 * @see com.rubecula.jexpression.EvalExpression#getExpressionTerms()
	 */
    public Term[] getExpressionTerms() {
        return evaluator.getExpressionTerms();
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#getIdentifier()
	 */
    public String getIdentifier() {
        return evaluator.getIdentifier();
    }

    /**
	 * @return {@link Notation#Algebraic}
	 * @see com.rubecula.jexpression.Evaluator#getNotation()
	 */
    public Notation getNotation() {
        return Notation.Algebraic;
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#getSymbols()
	 */
    public Collection<String> getSymbols() throws JexpressionException {
        return evaluator.getSymbols();
    }

    /**
	 * @throws JexpressionException
	 * @see com.rubecula.jexpression.Evaluator#getToken(com.rubecula.jexpression.Concept)
	 */
    public String getToken(final Concept concept) throws JexpressionException {
        return evaluator.getToken(concept);
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#getValue()
	 */
    public Number getValue() throws JexpressionException {
        return evaluator.getValue();
    }

    /**
	 * @return true
	 * @see com.rubecula.jexpression.EvalExpressionMutable#isMutable()
	 */
    public boolean isMutable() {
        return true;
    }

    /**
	 * @see com.rubecula.jexpression.Evaluator#removeSymbol(java.lang.String)
	 */
    public Object removeSymbol(final String name) throws JexpressionException {
        return evaluator.removeSymbol(name);
    }

    /**
	 * @param tokens
	 * @throws JexpressionException
	 */
    public void setExpression(final CharSequence... tokens) throws JexpressionException {
        evaluator = new Evaluator_Eval(tokens);
        onExpressionChange();
    }

    /**
	 * @param expression
	 * @throws JexpressionException
	 */
    public void setExpression(final String expression) throws JexpressionException {
        evaluator = new Evaluator_Eval(expression);
        onExpressionChange();
    }

    /**
	 * Notify the listeners that the expression has changed
	 */
    protected void onExpressionChange() {
        for (final Listener listener : _listeners) {
            listener.onExpressionChange(this);
        }
    }

    private final Collection<Listener> _listeners = new ArrayList<Listener>();

    private Evaluator evaluator;
}
