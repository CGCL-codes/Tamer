package org.thymeleaf.standard.expression;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.ObjectUtils;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class AndExpression extends BinaryOperationExpression {

    private static final long serialVersionUID = -6085038102412415337L;

    private static final Logger logger = LoggerFactory.getLogger(AndExpression.class);

    private static final String OPERATOR = "and";

    private static final String[] OPERATORS = new String[] { OPERATOR };

    private static final boolean[] LENIENCIES = new boolean[] { false };

    @SuppressWarnings("unchecked")
    private static final Class<? extends BinaryOperationExpression>[] OPERATOR_CLASSES = (Class<? extends BinaryOperationExpression>[]) new Class<?>[] { AndExpression.class };

    AndExpression(final Expression left, final Expression right) {
        super(left, right);
    }

    @Override
    public String getStringRepresentation() {
        return getStringRepresentation(OPERATOR);
    }

    protected static List<ExpressionParsingNode> composeAndExpression(final List<ExpressionParsingNode> decomposition, int inputIndex) {
        return composeBinaryOperationExpression(decomposition, inputIndex, OPERATORS, LENIENCIES, OPERATOR_CLASSES);
    }

    static Object executeAnd(final Arguments arguments, final TemplateResolution templateResolution, final AndExpression expression, final IStandardExpressionEvaluator expressionEvaluator) {
        if (logger.isTraceEnabled()) {
            logger.trace("[THYMELEAF][{}] Evaluating AND expression: \"{}\"", TemplateEngine.threadIndex(), expression.getStringRepresentation());
        }
        Object leftValue = Expression.execute(arguments, templateResolution, expression.getLeft(), expressionEvaluator);
        Object rightValue = Expression.execute(arguments, templateResolution, expression.getRight(), expressionEvaluator);
        final boolean leftBooleanValue = ObjectUtils.evaluateAsBoolean(leftValue);
        final boolean rightBooleanValue = ObjectUtils.evaluateAsBoolean(rightValue);
        return Boolean.valueOf(leftBooleanValue && rightBooleanValue);
    }
}
