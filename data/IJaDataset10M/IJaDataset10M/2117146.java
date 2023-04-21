package org.skunk.dav.client.method;

import org.skunk.dav.client.AbstractDAVMethod;
import org.skunk.dav.client.DAVMethodName;

/**
 * RFC 2518 does not modify the semantics of the HTTP GET method. The
 * following is excerpted from section 9 of RFC 2616:
 *
 * The GET method means retrieve whatever information (in the form of an
 * entity) is identified by the Request-URI. If the Request-URI refers
 * to a data-producing process, it is the produced data which shall be
 * returned as the entity in the response and not the source text of the
 * process, unless that text happens to be the output of the process.
 *
 * The semantics of the GET method change to a "conditional GET" if the
 * request message includes an If-Modified-Since, If-Unmodified-Since,
 * If-Match, If-None-Match, or If-Range header field. A conditional GET
 * method requests that the entity be transferred only under the
 * circumstances described by the conditional header field(s). The
 * conditional GET method is intended to reduce unnecessary network
 * usage by allowing cached entities to be refreshed without requiring
 * multiple requests or transferring data already held by the client.
 *
 * The semantics of the GET method change to a "partial GET" if the
 * request message includes a Range header field. A partial GET requests
 * that only part of the entity be transferred, as described in section
 * 14.35. The partial GET method is intended to reduce unnecessary
 * network usage by allowing partially-retrieved entities to be
 * completed without transferring data already held by the client.
 *
 * The response to a GET request is cacheable if and only if it meets
 * the requirements for HTTP caching described in section 13 [of RFC 2616].
 */
public class GetMethod extends AbstractDAVMethod {

    /**
   * Creates a new GetMethod using the provided URL.
   * @param url The URL to GET.
   */
    public GetMethod(String url) {
        super(url);
    }

    /**
     * Gets the name of the request method.
     * @return DAVMethodName.GET
     */
    public final DAVMethodName getRequestMethodName() {
        return DAVMethodName.GET;
    }
}
