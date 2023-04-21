package com.proemion.spring.protocol.http;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import com.proemion.spring.clustering.ClusteringConfiguration;
import com.proemion.spring.clustering.FailureHandler;
import com.proemion.spring.clustering.InvocationResult;
import com.proemion.spring.clustering.ProtocolDefinition;
import com.proemion.spring.clustering.RemoteService;
import com.proemion.spring.clustering.ServiceList;
import com.proemion.spring.clustering.InvocationResult.ResultType;
import com.proemion.spring.clustering.protocol.http.HttpInvokerHandler;
import org.aopalliance.intercept.Interceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.security.util.SimpleMethodInvocation;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("boxing")
public class HttpInvokerHandlerTest {

    private Server server;

    private final int testPort = 9903;

    private final String dummyService = "http://localhost:" + testPort + "/testdrive";

    private Object proxy;

    @DataProvider(name = "blockingMethods")
    public Object[][] getBlockingMethods() {
        return new Object[][] { { "blockingMethod", -1 }, { "blockingMethod2", 500 } };
    }

    private class Setup {

        private ClusteringConfiguration config;

        private ServiceList serviceList;

        private RemoteService aService1;

        private RemoteService aService2;

        private List<RemoteService> services;

        private HttpInvokerHandler protoHandler;

        private FailureHandler failureHandler;

        public void reset() {
            EasyMock.reset(serviceList, aService1, aService2, failureHandler);
        }

        public void replay() {
            EasyMock.replay(serviceList, aService1, aService2, failureHandler);
        }

        public void verify() {
            EasyMock.verify(serviceList, aService1, aService2, failureHandler);
        }
    }

    /**
   * Starts Remoting Webservice on port 9903 for test
   * @throws Exception
   */
    @SuppressWarnings("serial")
    @BeforeClass
    void startup() throws Exception {
        final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
        exporter.setServiceInterface(DummyInterface.class);
        exporter.setService(new DummyService());
        exporter.afterPropertiesSet();
        HttpServlet servlet = new HttpServlet() {

            @Override
            protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
                LocaleContextHolder.setLocale(request.getLocale());
                try {
                    exporter.handleRequest(request, response);
                } catch (HttpRequestMethodNotSupportedException ex) {
                    String[] supportedMethods = ex.getSupportedMethods();
                    if (supportedMethods != null) {
                        response.setHeader("Allow", StringUtils.arrayToDelimitedString(supportedMethods, ", "));
                    }
                    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ex.getMessage());
                } finally {
                    LocaleContextHolder.resetLocaleContext();
                }
            }
        };
        server = new Server(testPort);
        Context context = new Context(server, "/");
        context.addServlet(new ServletHolder(servlet), "/testdrive");
        server.start();
        proxy = new ProxyFactory(DummyInterface.class, new Interceptor() {
        }).getProxy();
    }

    /**
   * Shutdown the Webserver
   * @throws Exception
   */
    @AfterClass
    void shutdown() throws Exception {
        server.stop();
    }

    /**
   * Setup a complete mockup environment for testing
   * @return A Setup
   * @throws Exception
   */
    public Setup setUp() throws Exception {
        Setup setup = new Setup();
        setup.config = new ClusteringConfiguration();
        setup.config.setCodebaseUrl(null);
        setup.config.setServiceInterface(DummyInterface.class);
        setup.serviceList = createMock(ServiceList.class);
        setup.aService1 = createMock("service1", RemoteService.class);
        setup.aService2 = createMock("service2", RemoteService.class);
        setup.services = new ArrayList<RemoteService>();
        setup.services.add(setup.aService1);
        setup.services.add(setup.aService2);
        setup.failureHandler = createMock(FailureHandler.class);
        setup.protoHandler = new HttpInvokerHandler();
        setup.protoHandler.setDefaultTimeout(30000);
        setup.protoHandler.setConfiguration(setup.config);
        return setup;
    }

    @Test
    public void testSimpleCall() throws Exception {
        Setup setup = setUp();
        setup.reset();
        Capture<ProtocolDefinition> protoDefinition = recordProtocolDefinition(setup, dummyService);
        setup.replay();
        MethodInvocation invocation = new SimpleMethodInvocation(proxy, DummyInterface.class.getDeclaredMethod("simpleMethod"), null);
        InvocationResult result = setup.protoHandler.invoke(setup.aService1, invocation);
        setup.verify();
        assertSame(result.getResultType(), ResultType.SERVER_METHOD_RETURNED);
        assertEquals(result.getResult(), Boolean.TRUE);
        assertEquals(protoDefinition.getValue().getURI(), new URI(dummyService));
    }

    @Test
    public void testNonHttpURI() throws Exception {
        Setup setup = setUp();
        setup.reset();
        Capture<ProtocolDefinition> protoDefinition = recordProtocolDefinition(setup, "file:///");
        setup.replay();
        MethodInvocation invocation = new SimpleMethodInvocation(proxy, DummyInterface.class.getDeclaredMethod("simpleMethod"), null);
        InvocationResult result = setup.protoHandler.invoke(setup.aService1, invocation);
        setup.verify();
        assertSame(result.getResultType(), ResultType.REMOTING_ERROR);
        assertNotNull(result.getResult(), "Result Null, but should be Exception");
        assertEquals(result.getResult().getClass(), RemoteAccessException.class);
        assertEquals(protoDefinition.getValue().getURI(), new URI("file:///"));
    }

    @Test(dataProvider = "blockingMethods")
    public void testTimeout(final String methodName, final long paramTimeout) throws Exception {
        Setup setup = setUp();
        setup.reset();
        Capture<ProtocolDefinition> protoDefinition = recordProtocolDefinition(setup, dummyService);
        setup.replay();
        MethodInvocation invocation = new SimpleMethodInvocation(proxy, DummyInterface.class.getDeclaredMethod(methodName), null);
        long startNanos = System.nanoTime();
        InvocationResult result = setup.protoHandler.invoke(setup.aService1, invocation);
        long timeTaken = (System.nanoTime() - startNanos) / 1000000;
        setup.verify();
        long timeout;
        if (paramTimeout == -1) {
            timeout = setup.protoHandler.getDefaultTimeout();
        } else {
            timeout = paramTimeout;
        }
        assertTrue(timeTaken >= (timeout - 25), "timeout not used, was:" + timeTaken + " should be:" + timeout);
        assertTrue(timeTaken <= (timeout + 100), "took too long to break, was:" + timeTaken + " should be:" + timeout);
        assertSame(result.getResultType(), ResultType.REMOTING_TIMEOUT);
        assertNull(result.getResult(), "Result should be null");
        assertEquals(protoDefinition.getValue().getURI(), new URI(dummyService));
    }

    @Test
    public void testFailingMethod() throws Exception {
        Setup setup = setUp();
        setup.reset();
        Capture<ProtocolDefinition> protoDefinition = recordProtocolDefinition(setup, dummyService);
        setup.replay();
        MethodInvocation invocation = new SimpleMethodInvocation(proxy, DummyInterface.class.getDeclaredMethod("failingMethod"), null);
        InvocationResult result = setup.protoHandler.invoke(setup.aService1, invocation);
        setup.verify();
        assertSame(result.getResultType(), ResultType.SERVER_METHOD_EXCEPTION);
        assertNotNull(result.getResult(), "Result Null, but should be the thrown Exception");
        assertEquals(result.getResult().getClass(), NullPointerException.class);
        assertEquals(((Exception) result.getResult()).getMessage(), "Catch me if you can");
        assertEquals(protoDefinition.getValue().getURI(), new URI(dummyService));
    }

    private Capture<ProtocolDefinition> recordProtocolDefinition(final Setup setup, final String serviceUrl) throws URISyntaxException {
        final Capture<ProtocolDefinition> protoDefinition = new Capture<ProtocolDefinition>();
        expect(setup.aService1.getProtocolDefinition()).andReturn(null).once();
        expect(setup.aService1.getURI()).andReturn(new URI(serviceUrl)).anyTimes();
        setup.aService1.setProtocolDefinition(capture(protoDefinition));
        expect(setup.aService1.getProtocolDefinition()).andAnswer(new IAnswer<ProtocolDefinition>() {

            public ProtocolDefinition answer() throws Throwable {
                return protoDefinition.getValue();
            }
        }).anyTimes();
        return protoDefinition;
    }
}
