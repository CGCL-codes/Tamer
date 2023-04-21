package com.acv.webapp.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.BasicConfigurator;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockServletContext;

/**
 * TestCase that validates the URLRewriterFilter rewriting rules.
 */
public class UrlRewriterFilterTestCase extends MockObjectTestCase {

    static {
        BasicConfigurator.configure();
    }

    URLRewriterFilter filter;

    Mock request;

    Mock response;

    Mock filterChain;

    protected void setUp() throws Exception {
        super.setUp();
        filter = new URLRewriterFilter();
        MockServletContext context = new MockServletContext("");
        MockFilterConfig config = new MockFilterConfig(context);
        filter.setFilterConfig(config);
        filter.initFilterBean();
        request = new Mock(HttpServletRequest.class);
        response = new Mock(HttpServletResponse.class);
        filterChain = new Mock(FilterChain.class);
    }

    public void testIndexURL() throws ServletException, IOException {
        String url = "/en";
        String expectedURL = "/homepage.tiles?language=en";
        testURLRewrite(url, expectedURL);
    }

    public void testIndexURLWithQueryString() throws ServletException, IOException {
        String url = "/en?username=tim&password=123";
        String expectedURL = "/homepage.tiles?language=en&username=tim&password=123";
        testURLRewrite(url, expectedURL);
    }

    public void testMultipleElementURL() throws ServletException, IOException {
        String url = "/en/about_us/discover_acv/";
        String expectedURL = "/about_us-discover_acv.tiles?language=en";
        testURLRewrite(url, expectedURL);
    }

    public void testMultipleElementWithQueryStringURL() throws ServletException, IOException {
        String url = "/en/about_us/discover_acv?username=albert&password=123";
        String expectedURL = "/about_us-discover_acv.tiles?language=en&username=albert&password=123";
        testURLRewrite(url, expectedURL);
    }

    public void testIndexedContentDetailsURL() throws ServletException, IOException {
        String url = "/en/about_us/news/10064";
        String expectedURL = "/about_us-news-details.tiles?language=en&id=10064";
        testURLRewrite(url, expectedURL);
    }

    public void testIndexedContentDetailsWithQueryStringURL() throws ServletException, IOException {
        String url = "/en/about_us/news/10064?username=eugene&password=123";
        String expectedURL = "/about_us-news-details.tiles?language=en&id=10064&username=eugene&password=123";
        testURLRewrite(url, expectedURL);
    }

    /** Internal test procedure that wraps the simulation of the URLRewriterFilter **/
    private void testURLRewrite(String url, String expectedURL) {
        request.expects(atLeastOnce()).method("getServerName").will(returnValue("notlocalhost"));
        request.expects(atLeastOnce()).method("getServletPath").will(returnValue(url));
        request.expects(atLeastOnce()).method("getPathInfo").will(returnValue(""));
        request.expects(atLeastOnce()).method("getContextPath").will(returnValue(""));
        request.expects(atLeastOnce()).method("setAttribute").with(same("org.tuckey.web.filters.urlrewrite.RuleMatched"), same(Boolean.TRUE));
        request.expects(atLeastOnce()).method("getRequestDispatcher").will(returnValue(null));
        response.expects(atLeastOnce()).method("isCommitted").will(returnValue(false));
        try {
            filter.doFilterInternal((HttpServletRequest) request.proxy(), (HttpServletResponse) response.proxy(), (FilterChain) filterChain.proxy());
        } catch (ServletException e) {
            assertEquals("URL was not rewritted to the expected one", "unable to get request dispatcher for " + expectedURL, e.getMessage());
        } catch (IOException e) {
            fail("IOException occured ?");
        }
        request.verify();
    }
}
