package org.springframework.binding.expression.support;

import org.springframework.binding.expression.ExpressionVariable;
import org.springframework.binding.expression.ParserContext;

/**
 * A null object implementation of ParserContext. Mainly used internally by expression parser implementations when
 * <code>null</code> is passed in as a parser context value.
 * @author Keith Donad
 */
public final class NullParserContext implements ParserContext {

    /**
	 * The null parser context object instance; a singleton.
	 */
    public static final ParserContext INSTANCE = new NullParserContext();

    private NullParserContext() {
    }

    public Class getEvaluationContextType() {
        return null;
    }

    public Class getExpectedEvaluationResultType() {
        return null;
    }

    public ExpressionVariable[] getExpressionVariables() {
        return null;
    }

    public boolean isTemplate() {
        return false;
    }
}
