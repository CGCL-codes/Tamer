package com.meterware.httpunit.servletunit;

import com.meterware.httpunit.HttpUnitTest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.StatelessTest;
import junit.framework.TestSuite;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Tests for features pending addition to ServletUnit.
 */
public class NewServletUnitTests extends HttpUnitTest {

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(NewServletUnitTests.class);
    }

    public NewServletUnitTests(String name) {
        super(name);
    }

    /**
     * test bug report [ 1534234 ] HttpServletResponse.isCommitted() always false? (+patch)
     * by Olaf Klischat?
     * @throws Exception on unexpected error
     */
    public void testIsCommitted() throws Exception {
        ServletRunner sr = new ServletRunner();
        WebRequest request = new GetMethodWebRequest("http://localhost/servlet/" + CheckIsCommittedServlet.class.getName());
        WebResponse response = sr.getResponse(request);
        assertTrue("The response should be committed", CheckIsCommittedServlet.isCommitted);
    }

    /**
     * helper Servlet for bug report 1534234
     */
    public static class CheckIsCommittedServlet extends HttpServlet {

        public static boolean isCommitted;

        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("text/html");
            PrintWriter pw = resp.getWriter();
            pw.println("anything");
            pw.flush();
            pw.close();
            isCommitted = resp.isCommitted();
        }
    }
}
