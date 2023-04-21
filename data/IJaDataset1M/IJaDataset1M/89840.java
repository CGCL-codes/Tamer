package com.google.api.gwt.shared;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

/**
 * Provides access to Google API endpoints for RequestFactory.
 *
 * @deprecated Use {@link com.google.api.gwt.client.GoogleApiRequestTransport}.
 *
 * @author jasonhall@google.com (Jason Hall)
 */
@Deprecated
public interface GoogleApiRequestTransport<T extends GoogleApiRequestTransport<T>> extends RequestTransport {

    void create(@SuppressWarnings("rawtypes") Receiver<GoogleApiRequestTransport> receiver);

    T setBaseUrl(String baseUrl);

    T setAccessToken(String accessToken);

    T setApiAccessKey(String apiAccessKey);

    T setApplicationName(String applicationName);
}
