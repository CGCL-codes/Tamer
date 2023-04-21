package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.HeadMethod;

/**
 * @author Rodney Waldhoff
 * @author <a href="mailto:jsdever@apache.org">Jeff Dever</a>
 * @author Ortwin Gl�ck
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * @version $Revision: 12834 $ $Date: 2003-10-30 20:32:58 +0000 (Thu, 30 Oct 2003) $
 */
public class TestMethodsNoHost extends TestCase {

    static final String NAME = "name", VALUE = "value";

    static final String NAME0 = "name0", VALUE0 = "value0";

    static final String NAME1 = "name1", VALUE1 = "value1";

    static final String NAME2 = "name2", VALUE2 = "value2";

    static final NameValuePair PAIR = new NameValuePair(NAME, VALUE);

    static final NameValuePair PAIR0 = new NameValuePair(NAME0, VALUE0);

    static final NameValuePair PAIR1 = new NameValuePair(NAME1, VALUE1);

    static final NameValuePair PAIR2 = new NameValuePair(NAME2, VALUE2);

    public TestMethodsNoHost(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestMethodsNoHost.class);
    }

    public void testPostParametersEncoding() throws IOException {
        PostMethod post = new PostMethod();
        post.setRequestBody(new NameValuePair[] { PAIR });
        assertEquals("name=value", post.getRequestBodyAsString());
        post.setRequestBody(new NameValuePair[] { PAIR, PAIR1, PAIR2 });
        assertEquals("name=value&name1=value1&name2=value2", post.getRequestBodyAsString());
        post.setRequestBody(new NameValuePair[] { PAIR, PAIR1, PAIR2, new NameValuePair("hasSpace", "a b c d") });
        assertEquals("name=value&name1=value1&name2=value2&hasSpace=a+b+c+d", post.getRequestBodyAsString());
    }

    public void testPostSetRequestBody() throws Exception {
        PostMethod post = new PostMethod("/foo");
        String body = "this+is+the+body";
        post.setRequestBody(body);
        assertEquals(body, post.getRequestBodyAsString());
    }

    public void testHttpMethodBasePaths() throws Exception {
        HttpMethod simple = new SimpleHttpMethod();
        String[] paths = { "/some/absolute/path", "../some/relative/path", "/", "/some/path/with?query=string" };
        for (int i = 0; i < paths.length; i++) {
            simple.setPath(paths[i]);
            assertEquals(paths[i], simple.getPath());
        }
    }

    public void testHttpMethodBaseDefaultPath() throws Exception {
        HttpMethod simple = new SimpleHttpMethod();
        assertEquals("/", simple.getPath());
        simple.setPath("");
        assertEquals("/", simple.getPath());
        simple.setPath(null);
        assertEquals("/", simple.getPath());
    }

    public void testHttpMethodBasePathConstructor() throws Exception {
        HttpMethod simple = new SimpleHttpMethod();
        assertEquals("/", simple.getPath());
        simple = new SimpleHttpMethod("");
        assertEquals("/", simple.getPath());
        simple = new SimpleHttpMethod("/some/path/");
        assertEquals("/some/path/", simple.getPath());
    }

    /** Tests response with a Trasfer-Encoding and Content-Length */
    public void testHttpMethodBaseTEandCL() throws Exception {
        SimpleHttpConnection conn = new SimpleHttpConnection();
        String headers = "HTTP/1.1 200 OK\r\n" + "Date: Wed, 28 Mar 2001 05:05:04 GMT\r\n" + "Connection: close\r\n" + "Transfer-Encoding: chunked\r\n" + "Content-Length: 1\r\n";
        String body = "0a\r\n1234567890\r\n3\r\n123\r\n0\r\n";
        conn.addResponse(headers, body);
        conn.open();
        HttpMethodBase method = new GetMethod("/");
        method.execute(new HttpState(), conn);
        String responseBody = method.getResponseBodyAsString();
        conn.assertNotOpen();
        assertEquals("1234567890123", responseBody);
    }

    public void testConnectionAutoClose() throws Exception {
        SimpleHttpConnection conn = new SimpleHttpConnection();
        String headers = "HTTP/1.1 200 OK\r\n" + "Date: Wed, 28 Mar 2001 05:05:04 GMT\r\n" + "Connection: close\r\n";
        StringBuffer buffer = new StringBuffer(8200);
        for (int i = 0; i < 8200; i++) {
            buffer.append('A');
        }
        String body = buffer.toString();
        conn.addResponse(headers, body);
        conn.open();
        HttpMethodBase method = new GetMethod("/");
        method.execute(new HttpState(), conn);
        Reader response = new InputStreamReader(method.getResponseBodyAsStream());
        int c;
        while ((c = response.read()) != -1) {
            assertEquals((int) 'A', c);
        }
        conn.assertNotOpen();
        HeadMethod headMethod = new HeadMethod("/");
        conn.addResponse(headers, "");
        try {
            headMethod.execute(new HttpState(), conn);
            conn.assertNotOpen();
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Unable to execute method : " + t.toString());
        }
    }

    public void testSetGetQueryString1() {
        HttpMethod method = new GetMethod();
        String qs1 = "name1=value1&name2=value2";
        method.setQueryString(qs1);
        assertEquals(qs1, method.getQueryString());
    }

    public void testQueryURIEncoding() {
        HttpMethod method = new GetMethod("http://server/servlet?foo=bar&baz=schmoo");
        assertEquals("foo=bar&baz=schmoo", method.getQueryString());
    }

    public void testSetGetQueryString2() {
        HttpMethod method = new GetMethod();
        NameValuePair[] q1 = new NameValuePair[] { new NameValuePair("name1", "value1"), new NameValuePair("name2", "value2") };
        method.setQueryString(q1);
        String qs1 = "name1=value1&name2=value2";
        assertEquals(qs1, method.getQueryString());
    }

    /**
     * Make sure that its OK to call releaseConnection if the connection has not been.
     */
    public void testReleaseConnection() {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod("http://bogus.url/path/");
        method.releaseConnection();
    }

    /** 
     * Tests empty body response
     */
    public void testEmptyBodyAsString() throws Exception {
        SimpleHttpConnection conn = new SimpleHttpConnection();
        String headers = "HTTP/1.1 200 OK\r\n" + "Connection: close\r\n" + "Transfer-Encoding: chunked\r\n" + "Content-Length: 0\r\n";
        String body = "";
        conn.addResponse(headers, body);
        conn.open();
        HttpMethodBase method = new GetMethod("/");
        method.execute(new HttpState(), conn);
        String response = method.getResponseBodyAsString();
        assertNull(response);
    }

    public void testEmptyBodyAsByteArray() throws Exception {
        SimpleHttpConnection conn = new SimpleHttpConnection();
        String headers = "HTTP/1.1 200 OK\r\n" + "Connection: close\r\n" + "Transfer-Encoding: chunked\r\n" + "Content-Length: 0\r\n";
        String body = "";
        conn.addResponse(headers, body);
        conn.open();
        HttpMethodBase method = new GetMethod("/");
        method.execute(new HttpState(), conn);
        byte[] response = method.getResponseBody();
        assertNull(response);
    }
}
