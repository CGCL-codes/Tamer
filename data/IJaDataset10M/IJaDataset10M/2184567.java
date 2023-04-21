package neembuu.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.impl.nio.DefaultClientIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.protocol.BufferingHttpClientHandler;
import org.apache.http.nio.protocol.EventListener;
import org.apache.http.nio.protocol.HttpRequestExecutionHandler;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.SessionRequest;
import org.apache.http.nio.reactor.SessionRequestCallback;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

/**
 * Elemental example for executing HTTP requests using the non-blocking I/O model.
 * <p>
 * Please note the purpose of this application is demonstrate the usage of HttpCore APIs.
 * It is NOT intended to demonstrate the most efficient way of building an HTTP client.
 *
 *
 * @version $Revision$
 */
public class NonBlockingHttpClient {

    public static void main(String[] args) throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000).setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000).setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024).setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false).setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true).setParameter(CoreProtocolPNames.USER_AGENT, "HttpComponents/1.1");
        final ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(2, params);
        BasicHttpProcessor httpproc = new BasicHttpProcessor();
        httpproc.addInterceptor(new RequestContent());
        httpproc.addInterceptor(new RequestTargetHost());
        httpproc.addInterceptor(new RequestConnControl());
        httpproc.addInterceptor(new RequestUserAgent());
        httpproc.addInterceptor(new RequestExpectContinue());
        CountDownLatch requestCount = new CountDownLatch(3);
        BufferingHttpClientHandler handler = new BufferingHttpClientHandler(httpproc, new MyHttpRequestExecutionHandler(requestCount), new DefaultConnectionReuseStrategy(), params);
        handler.setEventListener(new EventLogger());
        final IOEventDispatch ioEventDispatch = new DefaultClientIOEventDispatch(handler, params);
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    ioReactor.execute(ioEventDispatch);
                } catch (InterruptedIOException ex) {
                    System.err.println("Interrupted");
                } catch (IOException e) {
                    System.err.println("I/O error: " + e.getMessage());
                }
                System.out.println("Shutdown");
            }
        });
        t.start();
        SessionRequest[] reqs = new SessionRequest[3];
        reqs[0] = ioReactor.connect(new InetSocketAddress("www.yahoo.com", 80), null, new HttpHost("www.yahoo.com"), new MySessionRequestCallback(requestCount));
        reqs[1] = ioReactor.connect(new InetSocketAddress("www.google.com", 80), null, new HttpHost("www.google.ch"), new MySessionRequestCallback(requestCount));
        reqs[2] = ioReactor.connect(new InetSocketAddress("www.apache.org", 80), null, new HttpHost("www.apache.org"), new MySessionRequestCallback(requestCount));
        requestCount.await();
        System.out.println("Shutting down I/O reactor");
        ioReactor.shutdown();
        System.out.println("Done");
    }

    static class MyHttpRequestExecutionHandler implements HttpRequestExecutionHandler {

        private static final String REQUEST_SENT = "request-sent";

        private static final String RESPONSE_RECEIVED = "response-received";

        private final CountDownLatch requestCount;

        public MyHttpRequestExecutionHandler(final CountDownLatch requestCount) {
            super();
            this.requestCount = requestCount;
        }

        public void initalizeContext(final HttpContext context, final Object attachment) {
            HttpHost targetHost = (HttpHost) attachment;
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, targetHost);
        }

        public void finalizeContext(final HttpContext context) {
            Object flag = context.getAttribute(RESPONSE_RECEIVED);
            if (flag == null) {
                requestCount.countDown();
            }
        }

        public HttpRequest submitRequest(final HttpContext context) {
            HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            Object flag = context.getAttribute(REQUEST_SENT);
            if (flag == null) {
                context.setAttribute(REQUEST_SENT, Boolean.TRUE);
                System.out.println("--------------");
                System.out.println("Sending request to " + targetHost);
                System.out.println("--------------");
                return new BasicHttpRequest("GET", "/");
            } else {
                return null;
            }
        }

        public void handleResponse(final HttpResponse response, final HttpContext context) {
            HttpEntity entity = response.getEntity();
            try {
                String content = EntityUtils.toString(entity);
                System.out.println("--------------");
                System.out.println(response.getStatusLine());
                System.out.println("--------------");
                System.out.println("Document length: " + content.length());
                System.out.println("--------------");
            } catch (IOException ex) {
                System.err.println("I/O error: " + ex.getMessage());
            }
            context.setAttribute(RESPONSE_RECEIVED, Boolean.TRUE);
            requestCount.countDown();
        }
    }

    static class MySessionRequestCallback implements SessionRequestCallback {

        private final CountDownLatch requestCount;

        public MySessionRequestCallback(final CountDownLatch requestCount) {
            super();
            this.requestCount = requestCount;
        }

        public void cancelled(final SessionRequest request) {
            System.out.println("Connect request cancelled: " + request.getRemoteAddress());
            this.requestCount.countDown();
        }

        public void completed(final SessionRequest request) {
        }

        public void failed(final SessionRequest request) {
            System.out.println("Connect request failed: " + request.getRemoteAddress());
            this.requestCount.countDown();
        }

        public void timeout(final SessionRequest request) {
            System.out.println("Connect request timed out: " + request.getRemoteAddress());
            this.requestCount.countDown();
        }
    }

    static class EventLogger implements EventListener {

        public void connectionOpen(final NHttpConnection conn) {
            System.out.println("Connection open: " + conn);
        }

        public void connectionTimeout(final NHttpConnection conn) {
            System.out.println("Connection timed out: " + conn);
        }

        public void connectionClosed(final NHttpConnection conn) {
            System.out.println("Connection closed: " + conn);
        }

        public void fatalIOException(final IOException ex, final NHttpConnection conn) {
            System.err.println("I/O error: " + ex.getMessage());
        }

        public void fatalProtocolException(final HttpException ex, final NHttpConnection conn) {
            System.err.println("HTTP error: " + ex.getMessage());
        }
    }
}
