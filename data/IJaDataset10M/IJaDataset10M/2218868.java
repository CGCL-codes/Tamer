package com.volantis.mcs.expression.functions.request;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.shared.dependency.DependencyContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;

/**
 * Represents the request:getHeaders function call within an expression
 * environment.
 */
public class GetHeadersFunction extends AbstractRequestExpressionFunction {

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER = new DefaultValueProvider();

    /**
     * Initializes the new instance with the given parameters.
     *
     */
    public GetHeadersFunction() {
    }

    protected String getFunctionName() {
        return "getHeaders";
    }

    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    /**
     * The given named header's set of values, if any, is extracted from the
     * specified requestContext and returned. If the requestContext doesn't
     * contain headers or the given named header doesn't exist, the specified
     * default value is returned instead.
     *
     * @param name           the name of the header to be obtained
     * @param defaultValue   the value to use if the header cannot be found
     * @return the header's set of values or the default value if the header is
     *         not found
     */
    protected Value execute(ExpressionContext expressionContext, MarinerRequestContext requestContext, String name, Value defaultValue) {
        ExpressionFactory factory = expressionContext.getFactory();
        Value value = defaultValue;
        EnvironmentContext environmentContext = ContextInternals.getEnvironmentContext(requestContext);
        Enumeration enumeration = environmentContext.getHeaders(name);
        List headers;
        if (enumeration == null || !enumeration.hasMoreElements()) {
            headers = Collections.EMPTY_LIST;
        } else {
            headers = new ArrayList();
            while (enumeration.hasMoreElements()) {
                headers.add(enumeration.nextElement());
            }
            int count = headers.size();
            Item[] items = new Item[count];
            for (int i = 0; i < count; i++) {
                String string = (String) headers.get(i);
                items[i] = factory.createStringValue(string);
            }
            value = factory.createSequence(items);
        }
        DependencyContext context = expressionContext.getDependencyContext();
        if (context.isTrackingDependencies()) {
            context.addDependency(new RequestHeadersDependency(name, headers));
        }
        return value;
    }
}
