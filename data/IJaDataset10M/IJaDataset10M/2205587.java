package com.esri.gpt.control.webharvest.common;

import org.apache.commons.httpclient.HttpClient;

/**
 * Common info.
 */
public class CommonInfo {

    private HttpClient batchHttpClient = new HttpClient();

    /**
   * Gets the underlying Apache HttpClient to be used for batch requests to the
   * same server.
   *
   * @return the batch client
   */
    public HttpClient getBatchHttpClient() {
        return this.batchHttpClient;
    }

    /**
   * Sets the underlying Apache HttpClient to be used for batch requests to the
   * same server.
   *
   * @param batchHttpClient the batch client
   */
    public void setBatchHttpClient(HttpClient batchHttpClient) {
        this.batchHttpClient = batchHttpClient;
    }

    /**
   * Destroys info.
   */
    public void destroy() {
        if (getBatchHttpClient() != null && getBatchHttpClient().getHttpConnectionManager() != null) {
            getBatchHttpClient().getHttpConnectionManager().closeIdleConnections(0);
        }
    }
}
