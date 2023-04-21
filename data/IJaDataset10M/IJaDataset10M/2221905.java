package com.fh.auge.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessResourceFailureException;

public class CommonsHttpRequestExecutor implements HttpRequestExecutor {

    private HttpClient httpClient;

    public CommonsHttpRequestExecutor(HttpClient httpClient) {
        super();
        this.httpClient = httpClient;
    }

    public CommonsHttpRequestExecutor() {
        super();
        this.httpClient = new HttpClient();
    }

    public CommonsHttpRequestExecutor(String proxyHost, int proxyPort) {
        super();
        this.httpClient = new HttpClient();
        this.httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
    }

    public String getContent(String url) {
        HttpMethod method = new GetMethod(url);
        String content = null;
        InputStream inputStream = null;
        try {
            httpClient.executeMethod(method);
            if (method.getStatusCode() != 200) throw new DataAccessResourceFailureException("http status code:" + method.getStatusLine() + " for " + url);
            inputStream = method.getResponseBodyAsStream();
            if (inputStream == null) throw new DataAccessResourceFailureException("no content for " + url);
            content = IOUtils.toString(inputStream);
            IOUtils.closeQuietly(inputStream);
        } catch (HttpException e) {
            throw new DataAccessResourceFailureException("can't connect to " + url, e);
        } catch (IOException e) {
            throw new DataAccessResourceFailureException("can't connect to " + url, e);
        } finally {
            method.releaseConnection();
        }
        return content;
    }
}
