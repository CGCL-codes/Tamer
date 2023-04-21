package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import org.impalaframework.web.integration.IntegrationServletConfig;
import org.springframework.web.context.WebApplicationContext;

public class InternalFrameworkIntegrationServletTest extends TestCase {

    private InternalFrameworkIntegrationServlet servlet;

    private ServletContext servletContext;

    private WebApplicationContext applicationContext;

    private HttpServlet delegateServlet;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new InternalFrameworkIntegrationServlet();
        servletContext = createMock(ServletContext.class);
        applicationContext = createMock(WebApplicationContext.class);
        delegateServlet = createMock(HttpServlet.class);
        servlet.setApplicationContext(applicationContext);
        servlet.setDelegateServlet(delegateServlet);
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
    }

    public void testInitDestroy() throws ServletException {
        replayMocks();
        servlet.init(new IntegrationServletConfig(new HashMap<String, String>(), servletContext, "myservlet"));
        servlet.destroy();
        verifyMocks();
    }

    public void testService() throws ServletException, IOException {
        expect(applicationContext.getClassLoader()).andReturn(null);
        delegateServlet.service(request, response);
        replayMocks();
        servlet.service(request, response);
        verifyMocks();
    }

    private void verifyMocks() {
        verify(servletContext);
        verify(applicationContext);
        verify(delegateServlet);
    }

    private void replayMocks() {
        replay(servletContext);
        replay(applicationContext);
        replay(delegateServlet);
    }
}
