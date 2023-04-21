package com.genia.toolbox.web.manager.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import javax.servlet.http.HttpServletRequest;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import com.genia.toolbox.basics.exception.technical.TechnicalException;
import com.genia.toolbox.web.context.WebContext;
import com.genia.toolbox.web.io.HttpServletRequestProxyFactory;

public class TestAbsoluteUrlManager {

    private static final Mockery context = new Mockery();

    private static final String contextPath = "/foo";

    private static final String absoluteUrls[] = { "/bar", "/baz", "/foo/bar", "/quz" };

    private static final String relativeUrls[] = { "../bar", "baz", "quz", "../../foo" };

    @Test
    public void testUrlManager() throws TechnicalException {
        final WebContext ungContext = context.mock(WebContext.class);
        final HttpServletRequest baseRequest = context.mock(HttpServletRequest.class);
        final HttpServletRequest request = HttpServletRequestProxyFactory.getHttpServletRequestProxy(baseRequest);
        context.checking(new Expectations() {

            {
                allowing(ungContext).getRequest();
                will(returnValue(request));
                allowing(baseRequest).getContextPath();
                will(returnValue(contextPath));
            }
        });
        final AbsoluteUrlManager urlManager = new AbsoluteUrlManager();
        urlManager.setContext(ungContext);
        for (final String url : absoluteUrls) {
            assertEquals(contextPath + url, urlManager.getUrl(url));
        }
        for (final String url : relativeUrls) {
            assertEquals(url, urlManager.getUrl(url));
        }
        try {
            urlManager.getUrl(new Object());
            fail("Should throw an exception");
        } catch (final Exception e) {
        }
        try {
            urlManager.getUrl(null);
            fail("Should throw an exception");
        } catch (final Exception e) {
        }
        try {
            urlManager.getUrl("        ");
            fail("Should throw an exception");
        } catch (final Exception e) {
        }
    }
}
