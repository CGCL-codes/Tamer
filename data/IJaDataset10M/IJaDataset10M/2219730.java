package com.agilejava.bignumbers.plexus;

import com.agilejava.bignumbers.core.Expression;
import com.agilejava.bignumbers.core.ExpressionBuilder;
import com.agilejava.bignumbers.core.UnknownVariableException;
import com.agilejava.bignumbers.core.VariableResolver;
import java.lang.reflect.AnnotatedElement;
import java.math.BigDecimal;

/**
 * An <code>Expression</code> implementation that builds itself from annotation
 * metadata from the field in which it will be injected.
 *
 * @author Wilfred Springer
 */
public class PlexusExpression implements Expression, Injectable {

    /**
   * The actual expression to which this wrapper provides access. Note that
   * this implementation is set using the injection callback mechanisms.
   */
    private Expression expression;

    /**
   * The <code>ExpressionBuilder</code> that will construct the required
   * <code>Expression</code> instance.
   */
    private ExpressionBuilder builder;

    public BigDecimal evaluate(VariableResolver variableResolver) throws ArithmeticException, UnknownVariableException {
        return expression.evaluate(variableResolver);
    }

    public String[] getVariableNames() {
        return expression.getVariableNames();
    }

    public void injected(AnnotatedElement element) {
        String expr = element.getAnnotation(BigExpression.class).value();
        expression = builder.create(expr);
    }
}
