package org.mobicents.client.slee.resource.http;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * A wrapper for a {@link HttpClient}, which forbiddens access to the connection
 * manager.
 * 
 * @author martins
 * 
 */
public class HttpClientWrapper implements HttpClient {

    private final HttpClient httpClient;

    public HttpClientWrapper(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpClient getWrappedHttpClient() {
        return httpClient;
    }

    @Override
    public HttpResponse execute(HttpUriRequest arg0) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0);
    }

    @Override
    public HttpResponse execute(HttpUriRequest arg0, HttpContext arg1) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1);
    }

    @Override
    public HttpResponse execute(HttpHost arg0, HttpRequest arg1) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1);
    }

    @Override
    public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1);
    }

    @Override
    public HttpResponse execute(HttpHost arg0, HttpRequest arg1, HttpContext arg2) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1, arg2);
    }

    @Override
    public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1, HttpContext arg2) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1, arg2);
    }

    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1, ResponseHandler<? extends T> arg2) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1, arg2);
    }

    @Override
    public <T> T execute(HttpHost arg0, HttpRequest arg1, ResponseHandler<? extends T> arg2, HttpContext arg3) throws IOException, ClientProtocolException {
        return httpClient.execute(arg0, arg1, arg2);
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        throw new SecurityException("a SLEE application may not access the client's connection manager.");
    }

    @Override
    public HttpParams getParams() {
        return httpClient.getParams();
    }
}
