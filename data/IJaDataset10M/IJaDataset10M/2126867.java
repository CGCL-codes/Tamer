package org.apache.shindig.gadgets.http;

import com.google.inject.ImplementedBy;

/**
 * Cache of HttpResponse.
 *
 * Keys are HttpRequest, values are the HttpResponse.
 */
@ImplementedBy(DefaultHttpCache.class)
public interface HttpCache {

    HttpResponse getResponse(HttpRequest request);

    /**
   * Add a request/response pair to the cache.
   * 
   * @return true if the response was cached, false if the response was not cached.
   */
    boolean addResponse(HttpRequest request, HttpResponse response);

    HttpResponse removeResponse(HttpRequest key);

    /**
   * Create a string representation of the cache key.  If two requests are cache equivalent (a
   * response to one request can be used to respond to the other request), their keys are
   * guaranteed to be identical.
   * 
   * Identical keys do not guarantee that two requests are cache equivalent.
   */
    String createKey(HttpRequest request);
}
