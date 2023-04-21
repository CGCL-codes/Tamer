package org.mortbay.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.http.handler.DumpHandler;
import org.mortbay.http.handler.NotFoundHandler;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.http.handler.TestTEHandler;
import org.mortbay.util.LogSupport;
import org.mortbay.util.Resource;
import org.mortbay.util.TestCase;
import org.mortbay.util.ThreadPool;

/** Test against RFC 2616.
 *
 * @version $Id: TestRFC2616.java,v 1.36 2006/04/05 15:24:43 gregwilkins Exp $
 * @author Greg Wilkins (gregw)
 */
public class TestRFC2616 extends ThreadPool implements HttpListener {

    private static Log log = LogFactory.getLog(TestRFC2616.class);

    private HttpServer _server;

    private static File docRoot = null;

    private static TestFileData[] testFiles = null;

    public static final String defaultTestRoot = "testdocs";

    public static final String testFilePrefix = "alphabet";

    public static final String testFileSuffix = ".txt";

    class TestFileData {

        File file;

        String data;

        String name;

        String modDate;

        Resource resource;

        public TestFileData(File file, String data) throws IOException {
            File docRoot = new File(file.getParent());
            this.file = file;
            this.data = data;
            this.name = file.getName();
            if (!docRoot.exists()) {
                docRoot.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
            if (log.isDebugEnabled()) log.debug("created " + file.getPath());
            try {
                this.resource = Resource.newResource(new URL("file", "localhost", file.getAbsolutePath()));
                this.modDate = HttpFields.formatDate(resource.lastModified(), false);
            } catch (MalformedURLException mue) {
                log.warn(LogSupport.EXCEPTION, mue);
            }
        }
    }

    public static final String[] testFileChars = { "abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ" };

    public TestFileData[] initTestFileData(File docRoot) throws IOException {
        TestFileData[] testFiles = new TestFileData[testFileChars.length];
        for (int i = 0; i < testFileChars.length; i++) {
            testFiles[i] = new TestFileData(new File(docRoot, testFilePrefix + i + testFileSuffix), testFileChars[i]);
        }
        return testFiles;
    }

    public TestRFC2616() throws Exception {
        if (testFiles == null) {
            docRoot = new File(defaultTestRoot);
            testFiles = initTestFileData(docRoot);
        }
        setName("Test");
        setMinThreads(1);
        setMaxThreads(10);
        setMaxIdleTimeMs(30000);
        _server = new HttpServer();
        _server.setTrace(true);
        HttpContext context = _server.getContext(null, "/");
        context.setResourceBase(docRoot.getName());
        context.addHandler(new TestTEHandler());
        context.addHandler(new RedirectHandler());
        ResourceHandler rh = new ResourceHandler();
        context.addHandler(rh);
        context.addHandler(new DumpHandler());
        context.addHandler(new NotFoundHandler());
        _server.addListener(this);
        _server.start();
    }

    public void setHttpServer(HttpServer s) {
    }

    public HttpServer getHttpServer() {
        return _server;
    }

    public ServerSocket getServerSocket() {
        return null;
    }

    public String getDefaultScheme() {
        return "jettytest";
    }

    public void setHost(String h) {
    }

    public String getHost() {
        return "localhost";
    }

    public void setPort(int p) {
    }

    public int getPort() {
        return 0;
    }

    public int getBufferSize() {
        return 4096;
    }

    public int getBufferReserve() {
        return 512;
    }

    public boolean isLowOnResources() {
        return false;
    }

    public boolean isOutOfResources() {
        return false;
    }

    public void persistConnection(HttpConnection connection) {
    }

    public String getResponses(String request) throws IOException {
        String responses = new String(getResponses(request.getBytes()));
        if (log.isDebugEnabled()) {
            System.out.println(request);
            System.out.println(responses);
        }
        return responses;
    }

    public byte[] getResponses(byte[] request) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(request);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpConnection connection = new HttpConnection(this, null, in, out, null);
        connection.handle();
        return out.toByteArray();
    }

    public static void test() {
        test3_3();
        test3_6();
        test3_9();
        test4_4();
        test5_2();
        test8_1();
        test8_2();
        test9_2();
        test9_4();
        test9_8();
        test10_2_7();
        test10_3();
        test14_16();
        test14_23();
        test14_35();
        test14_39();
        test19_6();
    }

    public static void test3_3() {
        TestCase t = new TestCase("RFC2616 3.3 Date/Time");
        try {
            HttpFields fields = new HttpFields();
            fields.put("D1", "Sun, 6 Nov 1994 08:49:37 GMT");
            fields.put("D2", "Sunday, 6-Nov-94 08:49:37 GMT");
            fields.put("D3", "Sun Nov  6 08:49:37 1994");
            Date d1 = new Date(fields.getDateField("D1"));
            Date d2 = new Date(fields.getDateField("D2"));
            Date d3 = new Date(fields.getDateField("D3"));
            t.checkEquals(d1, d2, "3.3.1 RFC 822 RFC 850");
            t.checkEquals(d2, d3, "3.3.1 RFC 850 ANSI C");
            fields.putDateField("Date", d1);
            t.checkEquals(fields.get("Date"), "Sun, 06 Nov 1994 08:49:37 GMT", "3.3.1 RFC 822 preferred");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test3_6() {
        TestCase t = new TestCase("RFC2616 3.6 Transfer Coding");
        String response = null;
        try {
            TestRFC2616 listener = new TestRFC2616();
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked,identity\n" + "Content-Type: text/plain\n" + "\015\012" + "5;\015\012" + "123\015\012\015\012" + "0;\015\012\015\012");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            t.checkContains(response, "HTTP/1.1 400 Bad", "Chunked last");
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked\n" + "Content-Type: text/plain\n" + "\n" + "2;\n" + "12\n" + "3;\n" + "345\n" + "0;\n\n" + "GET /R2 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked\n" + "Content-Type: text/plain\n" + "\n" + "4;\n" + "6789\n" + "5;\n" + "abcde\n" + "0;\n\n" + "GET /R3 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "12345", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "6789abcde", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "/R3", "3.6.1 Chunking");
            offset = 0;
            response = listener.getResponses("POST /R1 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked\n" + "Content-Type: text/plain\n" + "\n" + "2;\n" + "12\n" + "3;\n" + "345\n" + "0;\n\n" + "POST /R2 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked\n" + "Content-Type: text/plain\n" + "\n" + "4;\n" + "6789\n" + "5;\n" + "abcde\n" + "0;\n\n" + "GET /R3 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            t.checkNotContained(response, "HTTP/1.1 100", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "12345", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "6789abcde", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "/R3", "3.6.1 Chunking");
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked\n" + "Content-Type: text/plain\n" + "Connection: keep-alive\n" + "\n" + "3;\n" + "123\n" + "3;\n" + "456\n" + "0;\n\n" + "GET /R2 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "3.6.1 Chunking") + 10;
            offset = t.checkContains(response, offset, "123456", "3.6.1 Chunking");
            offset = t.checkContains(response, offset, "/R2", "3.6.1 Chunking") + 10;
        } catch (Exception e) {
            log.warn("FAIL: ", e);
            t.check(false, e.toString());
            if (response != null) log.warn(response);
        }
    }

    public static void test3_9() {
        TestCase t = new TestCase("RFC2616 3.9 Quality");
        try {
            HttpFields fields = new HttpFields();
            fields.put("Q", "bbb;q=0.5,aaa,ccc;q=0.002,d;q=0,e;q=0.0001,ddd;q=0.001,aa2,abb;q=0.7");
            Enumeration enm = fields.getValues("Q", ", \t");
            List list = HttpFields.qualityList(enm);
            t.checkEquals(HttpFields.valueParameters(list.get(0).toString(), null), "aaa", "Quality parameters");
            t.checkEquals(HttpFields.valueParameters(list.get(1).toString(), null), "aa2", "Quality parameters");
            t.checkEquals(HttpFields.valueParameters(list.get(2).toString(), null), "abb", "Quality parameters");
            t.checkEquals(HttpFields.valueParameters(list.get(3).toString(), null), "bbb", "Quality parameters");
            t.checkEquals(HttpFields.valueParameters(list.get(4).toString(), null), "ccc", "Quality parameters");
            t.checkEquals(HttpFields.valueParameters(list.get(5).toString(), null), "ddd", "Quality parameters");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test4_4() {
        TestCase t = new TestCase("RFC2616 4.4 Message Length");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: identity\n" + "Content-Type: text/plain\n" + "Content-Length: 5\n" + "\n" + "123\015\012" + "GET /R2 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK", "2. identity") + 10;
            offset = t.checkContains(response, offset, "/R1", "2. identity") + 3;
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK", "2. identity") + 10;
            offset = t.checkContains(response, offset, "/R2", "2. identity") + 3;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Transfer-Encoding: chunked\n" + "Content-Type: text/plain\n" + "Content-Length: 100\n" + "\n" + "3;\n" + "123\n" + "3;\n" + "456\n" + "0;\n" + "\n" + "GET /R2 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "Content-Type: text/plain\n" + "Content-Length: 6\n" + "\n" + "123456");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK", "3. ignore c-l") + 1;
            offset = t.checkContains(response, offset, "/R1", "3. ignore c-l") + 1;
            offset = t.checkContains(response, offset, "123456", "3. ignore c-l") + 1;
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK", "3. ignore c-l") + 1;
            offset = t.checkContains(response, offset, "/R2", "3. content-length") + 1;
            offset = t.checkContains(response, offset, "123456", "3. content-length") + 1;
            t.check(true, "Skip 411 checks as IE breaks this rule");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test5_2() {
        TestCase t = new TestCase("RFC2616 5.2 Virtual Hosts");
        try {
            TestRFC2616 listener = new TestRFC2616();
            HttpContext context = listener.getHttpServer().getContext("VirtualHost", "/path/*");
            context.addHandler(new DumpHandler());
            context.start();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /path/R1 HTTP/1.1\n" + "Host: localhost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "Default host") + 1;
            offset = t.checkContains(response, offset, "contextPath=", "Default host") + 1;
            offset = t.checkContains(response, offset, "pathInContext=/path/R1", "Default host") + 1;
            offset = 0;
            response = listener.getResponses("GET http://VirtualHost/path/R1 HTTP/1.1\n" + "Host: ignored\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "1. virtual host uri") + 1;
            offset = t.checkContains(response, offset, "contextPath=/path", "1. virtual host uri") + 1;
            offset = t.checkContains(response, offset, "pathInContext=/R1", "1. virtual host uri") + 1;
            offset = 0;
            response = listener.getResponses("GET /path/R1 HTTP/1.1\n" + "Host: VirtualHost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "2. virtual host field") + 1;
            offset = t.checkContains(response, offset, "contextPath=/path", "2. virtual host field") + 1;
            offset = t.checkContains(response, offset, "pathInContext=/R1", "2. virtual host field") + 1;
            offset = 0;
            response = listener.getResponses("GET /path/R1 HTTP/1.1\n" + "Host: ViRtUalhOst\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "2. virtual host field") + 1;
            offset = t.checkContains(response, offset, "contextPath=/path", "2. virtual host field") + 1;
            offset = t.checkContains(response, offset, "pathInContext=/R1", "2. virtual host field") + 1;
            offset = 0;
            response = listener.getResponses("GET /path/R1 HTTP/1.1\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 400", "3. no host") + 1;
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test8_1() {
        TestCase t = new TestCase("RFC2616 8.1 Persistent");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\015\012", "8.1.2 default") + 10;
            t.checkContains(response, offset, "Content-Length: ", "8.1.2 default");
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "\n" + "GET /R2 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n" + "GET /R3 HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\015\012", "8.1.2 default") + 1;
            offset = t.checkContains(response, offset, "/R1", "8.1.2 default") + 1;
            t.checkEquals(response.indexOf("/R3"), -1, "8.1.2.1 close");
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\015\012", "8.1.2.2 pipeline") + 11;
            offset = t.checkContains(response, offset, "Connection: close", "8.1.2.2 pipeline") + 1;
            offset = t.checkContains(response, offset, "/R2", "8.1.2.1 close") + 3;
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test8_2() {
        TestCase t = new TestCase("RFC2616 8.2 Transmission");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Expect: unknown\n" + "Content-Type: text/plain\n" + "Content-Length: 8\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 417", "8.2.3 expect failure") + 1;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Content-Type: text/plain\n" + "Content-Length: 8\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            t.checkEquals(response.indexOf("HTTP/1.1 100"), -1, "8.2.3 no expect no 100");
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "8.2.3 no expect no 100") + 1;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Expect: 100-continue\n" + "Content-Type: text/plain\n" + "Content-Length: 8\n" + "Connection: close\n" + "\n" + "123456\015\012");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK", "8.2.3 expect with body") + 1;
            t.checkEquals(response.indexOf("HTTP/1.1 100"), -1, "8.2.3 expect with body");
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "Expect: 100-continue\n" + "Content-Type: text/plain\n" + "Content-Length: 8\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 100 Continue", "8.2.3 expect 100") + 1;
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "8.2.3 expect 100") + 1;
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        } finally {
        }
    }

    public static void test9_2() {
        TestCase t = new TestCase("RFC2616 9.2 OPTIONS");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("OPTIONS * HTTP/1.1\n" + "Connection: close\n" + "Host: localhost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "200") + 1;
            offset = t.checkContains(response, offset, "Allow: GET, HEAD, POST, PUT, DELETE, MOVE, OPTIONS, TRACE", "Allow") + 1;
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test9_4() {
        TestCase t = new TestCase("RFC2616 9.4 HEAD");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String get = listener.getResponses("GET /R1 HTTP/1.0\n" + "Host: localhost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("GET: " + get);
            t.checkContains(get, 0, "HTTP/1.1 200", "GET");
            t.checkContains(get, 0, "Content-Type: text/html", "GET content");
            t.checkContains(get, 0, "Content-Length: ", "GET length");
            t.checkContains(get, 0, "<HTML>", "GET body");
            String head = listener.getResponses("HEAD /R1 HTTP/1.0\n" + "Host: localhost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("HEAD: " + head);
            t.checkContains(head, 0, "HTTP/1.1 200", "HEAD");
            t.checkContains(head, 0, "Content-Type: text/html", "HEAD content");
            t.checkContains(head, 0, "Content-Length: ", "HEAD length");
            t.checkEquals(head.indexOf("<HTML>"), -1, "HEAD no body");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test9_8() {
        TestCase t = new TestCase("RFC2616 9.8 TRACE");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("TRACE /path HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "200") + 1;
            offset = t.checkContains(response, offset, "Content-Type: message/http", "message/http") + 1;
            offset = t.checkContains(response, offset, "TRACE /path HTTP/1.1\r\n" + "Host: localhost\r\n", "Request");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test10_2_7() {
        TestCase t = new TestCase("RFC2616 10.2.7 206 Partial Content");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            response = listener.getResponses("GET /" + TestRFC2616.testFiles[0].name + " HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            boolean noRangeHasContentLocation = (response.indexOf("\r\nContent-Location: ") != -1);
            response = listener.getResponses("GET /" + TestRFC2616.testFiles[0].name + " HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "Range: bytes=1-3,bytes=6-9\n" + "Range: bytes=12-14\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = 0;
            offset = t.checkContains(response, offset, "HTTP/1.1 206 Partial Content\r\n", "1. proper 206 status code");
            offset = t.checkContains(response, offset, "--jetty", "4a. content type") + 2;
            offset = t.checkContains(response, offset, "Content-Type: text/plain", "4a. content type") + 2;
            offset = t.checkContains(response, offset, "Content-Range: bytes 1-3/26", "4a. content range") + 2;
            offset = t.checkContains(response, offset, TestRFC2616.testFiles[0].data.substring(1, 3 + 1), "6. subrange data");
            offset = t.checkContains(response, offset, "--jetty", "4b. content type") + 2;
            offset = t.checkContains(response, offset, "Content-Type: text/plain", "4bontent type") + 2;
            offset = t.checkContains(response, offset, "Content-Range: bytes 6-9/26", "4b. content range") + 2;
            offset = t.checkContains(response, offset, TestRFC2616.testFiles[0].data.substring(6, 9 + 1), "6. subrange data");
            offset = t.checkContains(response, offset, "--jetty", "4c. content type") + 2;
            offset = t.checkContains(response, offset, "Content-Type: text/plain", "4c. content type") + 2;
            offset = t.checkContains(response, offset, "Content-Range: bytes 12-14/26", "4c. content range") + 2;
            offset = t.checkContains(response, offset, TestRFC2616.testFiles[0].data.substring(12, 14 + 1), "6. subrange data");
            if (noRangeHasContentLocation) {
                offset = t.checkContains(response, offset, "Content-Location: ", "5. Content-Location header as with 200");
            }
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test10_3() {
        TestCase t = new TestCase("RFC2616 10.3 redirection");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /redirect HTTP/1.0\n" + "Connection: Keep-Alive\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 302", "302") + 1;
            t.checkContains(response, offset, "Location: /dump", "redirected");
            t.checkContains(response, offset, "Connection: close", "Connection: close");
            offset = 0;
            response = listener.getResponses("GET /redirect HTTP/1.1\n" + "Host: localhost\n" + "\n" + "GET /redirect HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 302", "302") + 1;
            t.checkContains(response, offset, "Location: /dump", "redirected");
            t.checkContains(response, offset, "Transfer-Encoding: chunked", "Transfer-Encoding: chunked");
            offset = t.checkContains(response, offset, "HTTP/1.1 302", "302") + 1;
            t.checkContains(response, offset, "Location: /dump", "redirected");
            t.checkContains(response, offset, "Connection: close", "closed");
            offset = 0;
            response = listener.getResponses("GET /redirect/content HTTP/1.0\n" + "Connection: Keep-Alive\n" + "\n" + "GET /redirect/content HTTP/1.0\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 302", "302") + 1;
            t.checkContains(response, offset, "Location: /dump", "redirected");
            t.checkContains(response, offset, "Connection: close", "close no content length");
            offset = 0;
            response = listener.getResponses("GET /redirect/content HTTP/1.1\n" + "Host: localhost\n" + "\n" + "GET /redirect/content HTTP/1.1\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 302", "302") + 1;
            t.checkContains(response, offset, "Location: /dump", "redirected");
            t.checkContains(response, offset, "Transfer-Encoding: chunked", "chunked content length");
            offset = t.checkContains(response, offset, "HTTP/1.1 302", "302") + 1;
            t.checkContains(response, offset, "Location: /dump", "redirected");
            t.checkContains(response, offset, "Connection: close", "closed");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public void checkContentRange(TestCase t, String tname, String path, String reqRanges, int expectedStatus, String expectedRange, String expectedData) {
        try {
            String response;
            int offset = 0;
            String byteRangeHeader = "";
            if (reqRanges != null) {
                byteRangeHeader = "Range: " + reqRanges + "\n";
            }
            response = getResponses("GET /" + path + " HTTP/1.1\n" + "Host: localhost\n" + byteRangeHeader + "Connection: close\n" + "\n");
            switch(expectedStatus) {
                case 200:
                    {
                        offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\r\n", tname + ".1. proper 200 OK status code");
                        break;
                    }
                case 206:
                    {
                        offset = t.checkContains(response, offset, "HTTP/1.1 206 Partial Content\r\n", tname + ".1. proper 206 Partial Content status code");
                        break;
                    }
                case 416:
                    {
                        offset = t.checkContains(response, offset, "HTTP/1.1 416 Requested Range Not Satisfiable\r\n", tname + ".1. proper 416 Requested Range not Satisfiable status code");
                        break;
                    }
            }
            if (expectedRange != null) {
                String expectedContentRange = "Content-Range: bytes " + expectedRange + "\r\n";
                offset = t.checkContains(response, offset, expectedContentRange, tname + ".2. content range " + expectedRange);
            }
            if (expectedStatus == 200 || expectedStatus == 206) {
                offset = t.checkContains(response, offset, expectedData, tname + ".3. subrange data: \"" + expectedData + "\"");
            }
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test14_16() {
        TestCase t = new TestCase("RFC2616 14.16 Content-Range");
        try {
            TestRFC2616 listener = new TestRFC2616();
            int id = 0;
            listener.checkContentRange(t, Integer.toString(id++), TestRFC2616.testFiles[0].name, null, 200, null, TestRFC2616.testFiles[0].data);
            String[] totallyBadRanges = { "bytes=a-b", "bytes=-1-2", "bytes=-1-2,2-3", "bytes=-", "bytes=-1-", "bytes=a-b,-1-1-1", "doublehalfwords=1-2" };
            for (int i = 0; i < totallyBadRanges.length; i++) {
                listener.checkContentRange(t, "BadRange" + i, TestRFC2616.testFiles[0].name, totallyBadRanges[i], 416, null, TestRFC2616.testFiles[0].data);
            }
            listener.checkContentRange(t, "bytes=5-8", TestRFC2616.testFiles[0].name, "bytes=5-8", 206, "5-8/26", TestRFC2616.testFiles[0].data.substring(5, 8 + 1));
            listener.checkContentRange(t, "bytes=5-8,50-60", TestRFC2616.testFiles[0].name, "bytes=5-8,50-60", 206, "5-8/26", TestRFC2616.testFiles[0].data.substring(5, 8 + 1));
            listener.checkContentRange(t, "bytes=50-60,5-8", TestRFC2616.testFiles[0].name, "bytes=50-60,5-8", 206, "5-8/26", TestRFC2616.testFiles[0].data.substring(5, 8 + 1));
            listener.checkContentRange(t, "bytes=50-60", TestRFC2616.testFiles[0].name, "bytes=50-60", 416, "*/26", null);
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test14_23() {
        TestCase t = new TestCase("RFC2616 14.23 host");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.0\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "200") + 1;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 400", "400") + 1;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host: localhost\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "200") + 1;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.1\n" + "Host:\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200", "200") + 1;
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test14_35() {
        TestCase t = new TestCase("RFC2616 14.35 Byte Ranges");
        try {
            TestRFC2616 listener = new TestRFC2616();
            listener.checkContentRange(t, "bytes=0-2", TestRFC2616.testFiles[0].name, "bytes=0-2", 206, "0-2/26", TestRFC2616.testFiles[0].data.substring(0, 2 + 1));
            listener.checkContentRange(t, "bytes=23-", TestRFC2616.testFiles[0].name, "bytes=23-", 206, "23-25/26", TestRFC2616.testFiles[0].data.substring(23, 25 + 1));
            listener.checkContentRange(t, "bytes=23-42", TestRFC2616.testFiles[0].name, "bytes=23-42", 206, "23-25/26", TestRFC2616.testFiles[0].data.substring(23, 25 + 1));
            listener.checkContentRange(t, "bytes=-3", TestRFC2616.testFiles[0].name, "bytes=-3", 206, "23-25/26", TestRFC2616.testFiles[0].data.substring(23, 25 + 1));
            listener.checkContentRange(t, "bytes=23-23,-2", TestRFC2616.testFiles[0].name, "bytes=23-23,-2", 206, "23-23/26", TestRFC2616.testFiles[0].data.substring(23, 23 + 1));
            listener.checkContentRange(t, "bytes=-1,-2,-3", TestRFC2616.testFiles[0].name, "bytes=-1,-2,-3", 206, "25-25/26", TestRFC2616.testFiles[0].data.substring(25, 25 + 1));
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public static void test14_39() {
        log.debug("NOT HANDLED RFC2616 14.39 TE");
        return;
    }

    public static void test19_6() {
        TestCase t = new TestCase("RFC2616 19.6 Keep-Alive");
        try {
            TestRFC2616 listener = new TestRFC2616();
            String response;
            int offset = 0;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.0\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\015\012", "19.6.2 default close") + 10;
            offset = 0;
            response = listener.getResponses("GET /R1 HTTP/1.0\n" + "Host: localhost\n" + "Connection: keep-alive\n" + "\n" + "GET /R2 HTTP/1.0\n" + "Host: localhost\n" + "Connection: close\n" + "\n" + "GET /R3 HTTP/1.0\n" + "Host: localhost\n" + "Connection: close\n" + "\n");
            if (log.isDebugEnabled()) log.debug("RESPONSE: " + response);
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\015\012", "19.6.2 Keep-alive 1") + 1;
            offset = t.checkContains(response, offset, "Connection: keep-alive", "19.6.2 Keep-alive 1") + 1;
            offset = t.checkContains(response, offset, "<HTML>", "19.6.2 Keep-alive 1") + 1;
            offset = t.checkContains(response, offset, "/R1", "19.6.2 Keep-alive 1") + 1;
            offset = t.checkContains(response, offset, "HTTP/1.1 200 OK\015\012", "19.6.2 Keep-alive 2") + 11;
            offset = t.checkContains(response, offset, "Connection: close", "19.6.2 Keep-alive close") + 1;
            offset = t.checkContains(response, offset, "/R2", "19.6.2 Keep-alive close") + 3;
            t.checkEquals(response.indexOf("/R3"), -1, "19.6.2 closed");
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
            t.check(false, e.toString());
        }
    }

    public void customizeRequest(HttpConnection connection, HttpRequest request) {
    }

    public static void main(String[] args) {
        try {
            TestRFC2616.test();
        } catch (Throwable e) {
            log.warn(LogSupport.EXCEPTION, e);
            new TestCase("org.mortbay.http.TestRFC2616").check(false, e.toString());
        } finally {
            TestCase.report();
        }
    }

    public class RedirectHandler extends AbstractHttpHandler {

        public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws HttpException, IOException {
            if (!super.isStarted()) return;
            if (request.getPath().startsWith("/redirect")) {
                if (request.getPath().startsWith("/redirect/content")) response.getOutputStream().write("Content".getBytes());
                response.sendRedirect("/dump");
                request.setHandled(true);
            }
        }
    }

    public boolean isIntegral(HttpConnection c) {
        return false;
    }

    public String getIntegralScheme() {
        return null;
    }

    public int getIntegralPort() {
        return 0;
    }

    public boolean isConfidential(HttpConnection c) {
        return false;
    }

    public String getConfidentialScheme() {
        return null;
    }

    public int getConfidentialPort() {
        return 0;
    }

    public HttpHandler getHttpHandler() {
        return null;
    }
}
