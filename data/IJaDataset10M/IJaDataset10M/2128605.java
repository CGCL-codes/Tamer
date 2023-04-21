package com.volantis.mcs.expression.functions.request;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.functions.AbstractFunction;

/**
 * Simple test case
 */
public class GetHeaderFunctionTestCase extends SingleValueFunctionTestAbstract {

    static String FUNCTION_NAME = "getHeader";

    static String FUNCTION_QNAME = "request:getHeader";

    protected String getFunctionQName() {
        return FUNCTION_QNAME;
    }

    protected String getURI() {
        return REQUEST_URI;
    }

    protected String getFunctionName() {
        return FUNCTION_NAME;
    }

    /**
     * Add a name/value pair to the request as a header.
     * @param name The header name
     * @param value The header value.
     */
    protected void addSingleValueExpectations(String name, String value) {
        environmentContextMock.expects.getHeader(name).returns(value).any();
    }

    protected AbstractFunction createTestableFunction(ExpressionFactory factory) {
        return new GetHeaderFunction();
    }
}
