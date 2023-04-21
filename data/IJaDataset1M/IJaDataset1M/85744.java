package org.shake.lastfm.net;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpEngine {

    private final Log log = LogFactory.getLog(HttpEngine.class);

    private HttpClient client;

    private HttpEngine() {
        this.client = new HttpClient(new SimpleHttpConnectionManager(true));
    }

    public static synchronized HttpEngine getInstance() {
        return new HttpEngine();
    }

    private synchronized HttpClient createHttpClient() {
        return this.client;
    }

    public String executeGetString(final String url) throws HttpEngineException, IllegalStateException {
        GetMethod getMethod = execute(url);
        try {
            return getMethod.getResponseBodyAsString();
        } catch (IOException e) {
            throw new HttpEngineException("Could not get respose from URL = <" + url + "> " + e.getMessage());
        }
    }

    public InputStream executeGet(final String url) throws HttpEngineException, IllegalStateException {
        GetMethod getMethod = execute(url);
        try {
            return getMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            throw new HttpEngineException("Could not get respose from URL = <" + url + "> " + e.getMessage());
        }
    }

    private GetMethod execute(final String url) throws HttpEngineException {
        GetMethod getMethod = new GetMethod(url);
        try {
            createHttpClient().executeMethod(getMethod);
            log.info("Executed: GET " + url);
        } catch (HttpException e) {
            throw new HttpEngineException("Could not execute GET URL = <" + url + "> " + e.getMessage());
        } catch (IOException e) {
            throw new HttpEngineException("Could not execute GET URL = <" + url + "> " + e.getMessage());
        }
        if (getMethod.getStatusCode() != HttpStatus.SC_OK) {
            log.error("HTTP GET status error. Message: " + getMethod.getStatusLine() + ". Expected status " + HttpStatus.SC_OK);
            throw new HttpEngineException("");
        }
        return getMethod;
    }
}
