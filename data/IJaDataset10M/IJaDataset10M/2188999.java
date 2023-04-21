package com.volantis.testtools.servletunit;

import com.meterware.httpunit.*;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import org.xml.sax.SAXException;

/**
 * A client for use with the servlet runner class, allowing the testing of servlets
 * without an actual servlet container. Testing can be done in one of two ways.
 * End-to-end testing works much like the HttpUnit package, except that only servlets
 * actually registered with the ServletRunner will be invoked.  It is also possible
 * to test servlets 'from the inside' by creating a ServletInvocationContext and then
 * calling any servlet methods which may be desired.  Even in this latter mode, end-to-end
 * testing is supported, but requires a call to this class's getResponse method to update
 * its cookies and frames.
 **/
public class ServletUnitClient extends WebClient {

    /**
     * Creates and returns a new servlet unit client instance.
     **/
    public static ServletUnitClient newClient(InvocationContextFactory factory) {
        return new ServletUnitClient(factory);
    }

    /**
     * Creates and returns a new invocation context from a GET request.
     **/
    public InvocationContext newInvocation(String requestString) throws IOException, MalformedURLException {
        return newInvocation(new GetMethodWebRequest(requestString));
    }

    /**
     * Creates and returns a new invocation context to test calling of servlet methods.
     **/
    public InvocationContext newInvocation(WebRequest request) throws IOException, MalformedURLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeMessageBody(request, baos);
        return _invocationContextFactory.newInvocation(this, request, getCookies(), this.getHeaderFields(), baos.toByteArray());
    }

    /**
     * Updates this client and returns the response which would be displayed by the
     * user agent. Note that this will typically be the same as that returned by the
     * servlet invocation unless that invocation results in a redirect request.
     **/
    public WebResponse getResponse(InvocationContext invocation) throws MalformedURLException, IOException, SAXException {
        updateClient(invocation.getServletResponse());
        return getFrameContents(invocation.getTarget());
    }

    /**
     * Creates a web response object which represents the response to the specified web request.
     **/
    protected WebResponse newResponse(WebRequest request) throws MalformedURLException, IOException {
        try {
            InvocationContext invocation = newInvocation(request);
            invocation.getServlet().service(invocation.getRequest(), invocation.getResponse());
            return invocation.getServletResponse();
        } catch (ServletException e) {
            throw new HttpInternalErrorException(request.getURL(), e);
        }
    }

    private InvocationContextFactory _invocationContextFactory;

    private static final Cookie[] NO_COOKIES = new Cookie[0];

    private ServletUnitClient(InvocationContextFactory factory) {
        _invocationContextFactory = factory;
    }

    private Cookie[] getCookies() {
        String cookieHeader = (String) getHeaderFields().get("Cookie");
        if (cookieHeader == null) return NO_COOKIES;
        Vector cookies = new Vector();
        StringTokenizer st = new StringTokenizer(cookieHeader, "=;");
        while (st.hasMoreTokens()) {
            String name = st.nextToken();
            if (st.hasMoreTokens()) {
                String value = st.nextToken();
                cookies.addElement(new Cookie(name, value));
            }
        }
        Cookie[] results = new Cookie[cookies.size()];
        cookies.copyInto(results);
        return results;
    }
}
