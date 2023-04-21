package org.jmonit.web;

import javax.servlet.http.HttpServlet;
import org.jmonit.AbstractRepositoryTestCase;
import org.jmonit.Monitor;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class RestServletTest extends AbstractRepositoryTestCase {

    public void testRequestRepositoryJson() throws Exception {
        HttpServlet servlet = new RestServlet();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Monitor myMonitor = repository.getMonitor("testRequestRepositoryJson");
        myMonitor.tag("perf").add(100);
        myMonitor.add(110);
        myMonitor.tag("test").add(90);
        request.setHeader("accept", "application/json");
        request.setPathInfo("/monitors/Statistics");
        servlet.service(request, response);
        assertEquals("application/json", response.getContentType());
        response.flushBuffer();
        String actual = response.getOutputStreamContent();
        assertTrue(actual.contains("name:\"testRequestRepositoryJson\""));
        assertTrue(actual.contains("\"test\""));
        assertTrue(actual.contains("hits:\"3\""));
        assertTrue(actual.contains("mean:\"100.0\""));
        assertTrue(actual.contains("min:\"90\""));
        assertTrue(actual.contains("max:\"110\""));
        assertTrue(actual.contains("standardDeviation:\"10.0\""));
        assertTrue(actual.contains("total:\"300\""));
    }

    public void testRequestRepositoryJsonNotANumber() throws Exception {
        HttpServlet servlet = new RestServlet();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Monitor myMonitor = repository.getMonitor("testRequestRepositoryJson");
        myMonitor.tag("perf").tag("test").tag("foo").tag("bar").add(100);
        request.setHeader("accept", "application/json");
        request.setPathInfo("/monitors/tag/test/Statistics+Concurrency");
        servlet.service(request, response);
        assertEquals("application/json", response.getContentType());
        response.flushBuffer();
        String actual = response.getOutputStreamContent();
        assertTrue(actual.contains("name:\"testRequestRepositoryJson\""));
        assertTrue(actual.contains("\"foo\""));
        assertTrue(actual.contains("\"bar\""));
        assertTrue(actual.contains("hits:\"1\""));
        assertTrue(actual.contains("mean:\"100.0\""));
        assertTrue(actual.contains("min:\"100\""));
        assertTrue(actual.contains("max:\"100\""));
        assertTrue(actual.contains("standardDeviation:\"NaN\""));
        assertTrue(actual.contains("total:\"100\""));
        assertTrue(actual.contains("actives:\"0\""));
        assertTrue(actual.contains("maxActives:\"0\""));
    }
}
