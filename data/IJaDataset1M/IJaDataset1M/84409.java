package org.gwanted.gwt.core.client;

import com.google.gwt.user.client.HTTPRequest;

/**
 * An extended HTTPRequest with syncronize download methods
 */
public class HttpRequestExt extends HTTPRequest {

    /**
   * Makes synchronous HTTP GET to a remote server.
   *
   * @param url the absolute url to GET
   * @return A string with the reponse content
   */
    public static native String syncGet(final String url);
}
