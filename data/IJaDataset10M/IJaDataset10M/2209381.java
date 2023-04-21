package ch.unifr.nio.framework.transform;

import java.nio.ByteBuffer;
import junit.framework.TestCase;

/**
 * tests that the AbstractHttpProxyRequestForwarder works like expected
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public class AbstractHttpProxyRequestForwarderTest extends TestCase {

    /**
     * tests that persistency is default
     * @throws Exception if an exception occurs
     */
    public void testDefaultPersistency() throws Exception {
        MyHttpProxyResponseForwarder responseForwarder = new MyHttpProxyResponseForwarder();
        MyHttpProxyRequestForwarder instance = new MyHttpProxyRequestForwarder(responseForwarder);
        String request = "GET http://www.example.org/index.html HTTP/1.1";
        instance.parseHeader(request);
        assertEquals(0, responseForwarder.getCloseClientCounter());
        assertEquals(0, responseForwarder.getCloseServerCounter());
    }

    /**
     * tests that the "Host" general-header field is only added when it is not
     * already present
     * @throws Exception if an exception occurs
     */
    public void testHostHeader() throws Exception {
        MyHttpProxyRequestForwarder instance = new MyHttpProxyRequestForwarder(null);
        String request = "GET http://www.example.org/index.html HTTP/1.1";
        instance.parseHeader(request);
        String expectedHeader = "GET /index.html HTTP/1.1\r\n" + "Via: HTTP/1.1 TEST\r\n" + "Host: www.example.org\r\n\r\n";
        String header = instance.getRequestHeader();
        System.out.println("header:\n" + header);
        assertEquals(expectedHeader, header);
        request = "GET http://www.example.org/index.html HTTP/1.1\r\n" + "Host: www.example.org";
        instance.parseHeader(request);
        header = instance.getRequestHeader();
        System.out.println("header:\n" + header);
        expectedHeader = "GET /index.html HTTP/1.1\r\n" + "Host: www.example.org\r\n" + "Via: HTTP/1.1 TEST\r\n\r\n";
        assertEquals(expectedHeader, header);
    }

    /**
     * tests that the "Via" general-header field is correctly inserted or
     * updated
     * @throws Exception if an exception occurs
     */
    public void testViaHeader() throws Exception {
        MyHttpProxyRequestForwarder instance = new MyHttpProxyRequestForwarder(null);
        String request = "GET http://www.example.org/index.html HTTP/1.1";
        instance.parseHeader(request);
        String expectedHeader = "GET /index.html HTTP/1.1\r\n" + "Via: HTTP/1.1 TEST\r\n" + "Host: www.example.org\r\n\r\n";
        assertEquals(expectedHeader, instance.getRequestHeader());
        request = "GET http://www.example.org/index.html HTTP/1.1\r\n" + "Via: HTTP/1.1 XYZ\r\n\r\n";
        instance.parseHeader(request);
        expectedHeader = "GET /index.html HTTP/1.1\r\n" + "Via: HTTP/1.1 XYZ, HTTP/1.1 TEST\r\n" + "Host: www.example.org\r\n\r\n";
        assertEquals(expectedHeader, instance.getRequestHeader());
    }

    /**
     * an example implementation of AbstractHttpProxyRequestForwarder
     */
    public class MyHttpProxyRequestForwarder extends AbstractHttpProxyRequestForwarder {

        private String requestHeader;

        /**
         * creates a new MyHttpProxyRequestForwarder
         * @param responseForwarder the corresponding forwarder for responses
         */
        public MyHttpProxyRequestForwarder(AbstractHttpProxyResponseForwarder responseForwarder) {
            super("TEST", responseForwarder);
        }

        @Override
        public void httpSyntaxError(String errorMessage) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void openConnection(String host, int port, byte[] initialData) {
            this.requestHeader = new String(initialData);
        }

        @Override
        protected void forwardData(ByteBuffer data, int size) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * @return the requestHeader
         */
        public String getRequestHeader() {
            return requestHeader;
        }
    }

    private class MyHttpProxyResponseForwarder extends AbstractHttpProxyResponseForwarder {

        private int closeClientCounter;

        private int closeServerCounter;

        public MyHttpProxyResponseForwarder() {
            super("test");
        }

        @Override
        public void closeClientConnection() {
            closeClientCounter++;
        }

        @Override
        public void closeServerConnection() {
            closeServerCounter++;
        }

        @Override
        protected void forwardData(ByteBuffer data, int size) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * @return the closeServerCounter
         */
        public int getCloseClientCounter() {
            return closeClientCounter;
        }

        /**
         * @return the closeServerCounter
         */
        public int getCloseServerCounter() {
            return closeServerCounter;
        }
    }
}
