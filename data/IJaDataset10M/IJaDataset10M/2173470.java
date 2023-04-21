package com.volantis.mcs.runtime.styling;

import com.volantis.mcs.runtime.layouts.MCSContainerInstanceFunction;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.compiler.FunctionResolverBuilder;

/**
 * The MCS specific styling functions.
 */
public class StylingFunctions {

    private static final FunctionResolver resolver;

    static {
        StylingFactory factory = StylingFactory.getDefaultInstance();
        FunctionResolverBuilder builder = factory.createFunctionResolverBuilder();
        builder.addFunction("mcs-container-instance", new MCSContainerInstanceFunction());
        resolver = builder.getResolver();
    }

    public static final FunctionResolver getResolver() {
        return resolver;
    }
}
