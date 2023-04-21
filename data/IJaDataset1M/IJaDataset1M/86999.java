package org.eclipse.tptp.test.tools.web.runner;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.eclipse.hyades.test.http.runner.HttpRequest;
import org.eclipse.hyades.test.http.runner.HttpResourceBundle;
import org.eclipse.hyades.test.http.runner.internal.exec.HttpRequestHandler;
import org.eclipse.hyades.test.http.runner.internal.exec.SSLHttpExecutor;

/**
 * @author marcelop
 * @since 1.0.2
 */
public class WebHttpExecutor {

    private static int iSSLClassesAvailable = -1;

    private String strLastHost = null;

    private int iLastPort = 0;

    private Socket socket = null;

    private InputStream from_server = null;

    private OutputStream to_server = null;

    private Object sslExecutor = null;

    private WebHttpRequestHandler httpRequestHandler = null;

    private int socketBufSize = 0;

    public int currentPageNumber;

    public int currentPageOrder;

    public String currentPageName;

    public long pageStart;

    public long pageEnd;

    public long pageResponseTime;

    public static final String RESPONSE_URL = "ResponseURL:";

    public static final String TIME = "Time:";

    public WebHttpExecutor() {
        iLastPort = 0;
    }

    public WebHttpResponse execute(HttpRequest request) throws Exception {
        WebHttpResponse response = new WebHttpResponse(request);
        if (iSSLClassesAvailable == -1) {
            try {
                Class.forName("javax.net.ssl.SSLSocket");
                iSSLClassesAvailable = 1;
            } catch (ClassNotFoundException cnf) {
                iSSLClassesAvailable = 0;
            }
        }
        try {
            request.getClass().getMethod("getThinkTime", null);
            long thinkTime = request.getThinkTime();
            if (thinkTime > 0) Thread.sleep(thinkTime);
        } catch (Exception e) {
        }
        if (httpRequestHandler == null) {
            httpRequestHandler = new WebHttpRequestHandler();
        }
        currentPageNumber = request.getPageNumber();
        currentPageOrder = request.getPageOrder();
        long start = System.currentTimeMillis();
        if ((currentPageOrder == HttpRequest.PAGE_START) || (currentPageOrder == HttpRequest.PAGE_ONLY)) {
            pageStart = start;
            currentPageName = request.getURL();
        }
        if (iSSLClassesAvailable == 1 && request.getSecure() == true) {
            SSLHttpExecutor ssl = null;
            if (sslExecutor == null) sslExecutor = new WebSSLHttpExecutor(httpRequestHandler);
            ssl = (SSLHttpExecutor) sslExecutor;
            ssl.execute(request, response);
            setResponseEndingData(request, response, start);
            return response;
        } else if (iSSLClassesAvailable == 0 && request.getSecure() == true) {
            System.out.println(HttpResourceBundle.SSL_NOTSUPPORTED);
        }
        String strHost = request.getHost();
        int port = request.getPort();
        if (port != iLastPort || strLastHost == null || strHost.regionMatches(0, strLastHost, 0, strLastHost.length()) != true) {
            if ((connectToServer(response, strHost, port)) == false) {
                response.setCode(-1);
                return response;
            }
        }
        if (httpRequestHandler.sendRequest(request, to_server) == false) {
            if (connectToServer(response, strHost, port) == false) {
                response.setCode(-1);
                return response;
            } else {
                if (httpRequestHandler.sendRequest(request, to_server) == false) {
                    response.setCode(-1);
                    return response;
                }
            }
        }
        httpRequestHandler.getServerResponse(request, response, from_server, socketBufSize);
        if (response.getCode() == 0) {
            if (connectToServer(response, strHost, port) == true) {
                if (httpRequestHandler.sendRequest(request, to_server) == true) {
                    httpRequestHandler.getServerResponse(request, response, from_server, socketBufSize);
                }
            }
        }
        if (response.getShouldCloseSocket() == true) strLastHost = null;
        setResponseEndingData(request, response, start);
        return response;
    }

    private boolean setResponseEndingData(HttpRequest request, WebHttpResponse response, long start) {
        long end = System.currentTimeMillis();
        if (currentPageOrder == HttpRequest.PAGE_END || currentPageOrder == HttpRequest.PAGE_ONLY) {
            pageEnd = end;
        }
        if (request.getPageOrder() == HttpRequest.PAGE_ONLY || request.getPageOrder() == HttpRequest.PAGE_END) {
            pageResponseTime = (pageEnd - pageStart);
            System.out.println(RESPONSE_URL + " " + currentPageName + " " + TIME + " " + pageResponseTime);
        } else pageResponseTime = -1;
        response.setElapsedTime(end - start);
        response.setPageResponseTime(pageResponseTime);
        return true;
    }

    private boolean connectToServer(WebHttpResponse response, String strHost, int port) {
        try {
            if (socket != null) socket.close();
            socket = new Socket(strHost, port);
            from_server = socket.getInputStream();
            to_server = socket.getOutputStream();
            socketBufSize = socket.getReceiveBufferSize();
            iLastPort = port;
            strLastHost = strHost;
        } catch (Exception e) {
            response.setCode(-1);
            response.setDetail(e.toString());
            return false;
        }
        return true;
    }
}
