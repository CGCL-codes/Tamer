package com.volantis.styling.impl.expressions;

import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.expressions.StylingFunction;
import java.util.Map;

/**
 * A simple map-based implementation of {@link FunctionResolver}.
 */
public class SimpleFunctionResolver implements FunctionResolver {

    /**
     * The functions recognised by this resolver.
     */
    private final Map functions;

    public SimpleFunctionResolver(Map functions) {
        this.functions = functions;
    }

    public StylingFunction resolve(String name) {
        return (StylingFunction) functions.get(name);
    }
}
