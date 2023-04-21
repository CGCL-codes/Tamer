package org.nexopenframework.context.local;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import org.nexopenframework.context.local.ThreadLocalMap;
import org.nexopenframework.context.local.ThreadLocalMapFilter;
import org.nexopenframework.context.local.ThreadLocalMapListener;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Easy Test case for {@link ThreadLocalMap} and derived strategies</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ThreadLocalMapTest extends TestCase {

    public void testHttpServletRequestAttributesFilter() throws ServletException, IOException {
        final ServletContext sc = new MockServletContext();
        MockHttpServletRequest req = new MockHttpServletRequest(sc);
        req.setAttribute("NOW", new Date());
        req.setMethod("POST");
        ThreadLocalMapFilter filter = new ThreadLocalMapFilter();
        filter.init(new FilterConfig() {

            public String getFilterName() {
                return "ThreadLocalFilter";
            }

            public String getInitParameter(String name) {
                return null;
            }

            public Enumeration getInitParameterNames() {
                return null;
            }

            public ServletContext getServletContext() {
                return sc;
            }
        });
        final MyServlet servlet = new MyServlet();
        servlet.init(new MockServletConfig(sc));
        filter.doFilter(req, new MockHttpServletResponse(), new FilterChain() {

            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                servlet.service(request, response);
            }
        });
        servlet.destroy();
    }

    public void testHttpSessionAttributesFilter() throws ServletException, IOException {
        final ServletContext sc = new MockServletContext();
        MockHttpServletRequest req = new MockHttpServletRequest(sc);
        req.setAttribute("NOW", new Date());
        req.setMethod("POST");
        req.getSession().setAttribute("some.value", "some value");
        ThreadLocalMapFilter filter = new ThreadLocalMapFilter();
        filter.init(new FilterConfig() {

            public String getFilterName() {
                return "ThreadLocalFilter";
            }

            public String getInitParameter(String name) {
                return (name.equals(ThreadLocalMapFilter.SAVE_SESSION_ATTRIBUTES)) ? "true" : null;
            }

            public Enumeration getInitParameterNames() {
                return null;
            }

            public ServletContext getServletContext() {
                return sc;
            }
        });
        final MySessionServlet servlet = new MySessionServlet();
        servlet.init(new MockServletConfig(sc));
        filter.doFilter(req, new MockHttpServletResponse(), new FilterChain() {

            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                servlet.service(request, response);
            }
        });
        servlet.destroy();
    }

    public void testListener() throws ServletException, IOException {
        ServletContext sc = new MockServletContext();
        MockHttpServletRequest req = new MockHttpServletRequest(sc);
        req.setAttribute("NOW", new Date());
        req.setMethod("POST");
        ThreadLocalMapListener listener = new ThreadLocalMapListener();
        ServletRequestEvent sre = new ServletRequestEvent(sc, req);
        listener.requestInitialized(sre);
        MyServlet servlet = new MyServlet();
        servlet.init(new MockServletConfig(sc));
        servlet.service(req, new MockHttpServletResponse());
        listener.requestDestroyed(sre);
        servlet.destroy();
    }

    private class MyServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Date d = (Date) ThreadLocalMap.get("NOW");
            assertNotNull(d);
            System.out.println("MyServlet.doGet() Now :: " + d);
        }
    }

    private class MySessionServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            Date d = (Date) ThreadLocalMap.get("NOW");
            assertNotNull(d);
            System.out.println("MyServlet.doGet() Now :: " + d);
            String value = (String) ThreadLocalMap.get("some.value");
            assertNotNull(value);
            assertEquals("some value", value);
        }
    }
}
