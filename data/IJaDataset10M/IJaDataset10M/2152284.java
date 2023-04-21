package com.volantis.shared.net.http;

import com.volantis.shared.net.http.client.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import java.io.IOException;

/**
 * Default implementation of {@link MethodExecuter}.
 */
public class MethodExecuterImpl implements MethodExecuter {

    /**
     * Underlying HTTP client.
     */
    private final HttpClient client;

    /**
     * Initialise.
     *
     * @param client The underlying HTTP client.
     */
    public MethodExecuterImpl(HttpClient client) {
        this.client = client;
    }

    public HttpStatusCode execute(GetMethod method) throws IOException {
        return client.executeMethod(method);
    }
}
