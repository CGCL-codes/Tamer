package com.volantis.xml.pipeline.sax.impl.operations.diselect;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

/**
 * A static helper class for diselect rules
 */
final class DIHelper {

    /**
     * Evaluates the string expressions and convert to a boolean value.
     * @param pipelineContext The context within which the expression should be
     * evaludated.
     * @param expressionString The expression as a string.
     * @return The result of evaluating the expression converted to a boolean.
     * @throws ExpressionException If the expression was invalid.
     */
    public static boolean evaluateExpression(XMLPipelineContext pipelineContext, String expressionString) throws ExpressionException {
        ExpressionFactory expressionFactory = ExpressionFactory.getDefaultInstance();
        ExpressionContext expressionContext = pipelineContext.getExpressionContext();
        Expression expression = expressionFactory.createExpressionParser().parse(expressionString);
        Value result = expression.evaluate(expressionContext);
        return PipelineExpressionHelper.fnBoolean(result.getSequence()).asJavaBoolean();
    }
}
