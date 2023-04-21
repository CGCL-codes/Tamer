package org.t2framework.t2.mock;

import org.t2framework.commons.mock.MockHttpServletRequest;
import org.t2framework.commons.mock.MockHttpServletRequestImpl;
import org.t2framework.commons.mock.MockHttpServletResponseImpl;
import org.t2framework.commons.mock.MockServletContextImpl;
import org.t2framework.t2.contexts.impl.RequestImpl;

/**
 * 
 * <#if locale="en">
 * <p>
 * Mock implementation of {@link RequestImpl}.
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class MockRequestImpl extends RequestImpl implements MockRequest {

    protected MockHttpServletRequest mockRequest;

    public MockRequestImpl(String requestPath) {
        this(null, requestPath);
    }

    public MockRequestImpl(String contextPath, String requestPath) {
        this(new MockHttpServletRequestImpl(new MockServletContextImpl(contextPath), requestPath));
    }

    protected MockRequestImpl(MockHttpServletRequest request) {
        super(request, new MockHttpServletResponseImpl(request));
        this.mockRequest = request;
    }

    @Override
    public void addParameter(String name, String value) {
        mockRequest.addParameter(name, value);
    }
}
