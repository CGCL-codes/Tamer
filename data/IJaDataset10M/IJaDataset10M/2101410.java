package org.apache.el;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import org.apache.el.lang.ELSupport;
import org.apache.el.lang.ExpressionBuilder;
import org.apache.el.util.MessageFactory;

/**
 * @see javax.el.ExpressionFactory
 * 
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public class ExpressionFactoryImpl extends ExpressionFactory {

    /**
     * 
     */
    public ExpressionFactoryImpl() {
        super();
    }

    public Object coerceToType(Object obj, Class type) {
        return ELSupport.coerceToType(obj, type);
    }

    public MethodExpression createMethodExpression(ELContext context, String expression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
        if (expectedParamTypes == null) {
            throw new NullPointerException(MessageFactory.get("error.method.nullParms"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createMethodExpression(expectedReturnType, expectedParamTypes);
    }

    public ValueExpression createValueExpression(ELContext context, String expression, Class expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
        }
        ExpressionBuilder builder = new ExpressionBuilder(expression, context);
        return builder.createValueExpression(expectedType);
    }

    public ValueExpression createValueExpression(Object instance, Class expectedType) {
        if (expectedType == null) {
            throw new NullPointerException(MessageFactory.get("error.value.expectedType"));
        }
        return new ValueExpressionLiteral(instance, expectedType);
    }
}
