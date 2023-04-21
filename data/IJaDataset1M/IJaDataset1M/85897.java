package com.volantis.mcs.expression.functions.request;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.shared.dependency.DependencyContext;
import java.util.Arrays;
import java.util.List;

/**
 * The GetParameters ExpressionFunction.
 */
public class GetParametersFunction extends AbstractRequestExpressionFunction {

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER = new DefaultValueProvider();

    /**
     * Initializes the new instance with the given parameters.
     *
     */
    public GetParametersFunction() {
    }

    protected String getFunctionName() {
        return "getParameters";
    }

    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    protected Value execute(ExpressionContext expressionContext, MarinerRequestContext requestContext, String name, Value defaultValue) {
        ExpressionFactory factory = expressionContext.getFactory();
        Value value = defaultValue;
        String params[] = requestContext.getParameterValues(name);
        if (params != null && params.length > 0) {
            Item[] items = new Item[params.length];
            for (int i = 0; i < params.length; i++) {
                items[i] = factory.createStringValue(params[i]);
            }
            value = factory.createSequence(items);
            DependencyContext context = expressionContext.getDependencyContext();
            if (context.isTrackingDependencies()) {
                List list = Arrays.asList(params);
                context.addDependency(new RequestParametersDependency(name, list));
            }
        }
        return value;
    }
}
