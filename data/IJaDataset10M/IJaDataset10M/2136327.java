package com.googlecode.estuary.httpd.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import com.googlecode.estuary.httpd.domain.HttpVersion;
import com.googlecode.estuary.httpd.domain.inet.Port;
import com.googlecode.estuary.httpd.domain.io.HttpOutputStream;
import com.googlecode.estuary.httpd.domain.request.AssistedHttpRequest;
import com.googlecode.estuary.httpd.domain.request.BasicHttpRequest;
import com.googlecode.estuary.httpd.domain.request.HttpRequest;
import com.googlecode.estuary.httpd.domain.response.AssistedHttpResponse;
import com.googlecode.estuary.httpd.domain.response.BasicHttpResponse;
import com.googlecode.estuary.httpd.domain.response.HttpStatusCode;
import com.googlecode.estuary.httpd.domain.response.HttpStatusLine;
import com.googlecode.estuary.httpd.domain.response.RequestAwareHttpResponse;

public class HttpServer {

    public HttpServer() {
    }

    public void runServer(HttpRequestHandler handler, Port port) throws IOException {
        ServerSocket ss = new ServerSocket(port.getPortNumber());
        while (true) {
            Socket socket = ss.accept();
            prepareAndDispatch(socket, handler);
        }
    }

    private void prepareAndDispatch(Socket socket, HttpRequestHandler handler) throws IOException {
        InputStream is = socket.getInputStream();
        HttpOutputStream os = new HttpOutputStream(socket.getOutputStream());
        AssistedHttpRequest httpRequest = new AssistedHttpRequest(new BasicHttpRequest(is));
        System.out.println(httpRequest);
        System.out.println("Writing response");
        AssistedHttpResponse httpResponse = new AssistedHttpResponse(new RequestAwareHttpResponse(new BasicHttpResponse(os), httpRequest));
        handler.handleRequest(httpRequest, httpResponse);
        is.close();
        os.close();
        socket.close();
    }

    public static final void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            dispatch(s);
        }
    }

    private static void dispatch(Socket s) throws IOException {
        InputStream is = s.getInputStream();
        HttpOutputStream os = new HttpOutputStream(s.getOutputStream());
        HttpRequest httpRequest = new BasicHttpRequest(is);
        System.out.println(httpRequest);
        System.out.println("Writing response");
        AssistedHttpResponse httpResponse = new AssistedHttpResponse(new RequestAwareHttpResponse(new BasicHttpResponse(os), httpRequest));
        HttpVersion HTTP_1_1 = new HttpVersion(1, 1);
        HttpStatusLine statusLine = new HttpStatusLine(HTTP_1_1, HttpStatusCode.OK);
        httpResponse.setStatusLine(statusLine);
        httpResponse.setContentType("text/plain");
        httpResponse.setContentLength(5);
        httpResponse.transmitHead();
        os.writeln("hello world");
        os.writeln();
        os.writeln("hello world");
        os.writeln();
        os.flush();
        is.close();
        os.close();
        s.close();
    }
}
