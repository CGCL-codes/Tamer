package org.wings.resource;

import org.wings.*;
import org.wings.session.SessionManager;

/**
 * Quite similar to the DefaultURLResource. Though this resource supports url rewriting for cookie-less sessions.
 * Thus it can address resources below the servlet path.
 */
public class SessionResource implements URLResource {

    private final SimpleURL url;

    /**
     * @param url The URL to access this ressource.
     */
    public SessionResource(String url) {
        this.url = new SimpleURL(url);
    }

    public SimpleURL getURL() {
        RequestURL requestURL = (RequestURL) SessionManager.getSession().getProperty("request.url");
        if (requestURL != null) {
            requestURL = (RequestURL) requestURL.clone();
            requestURL.setResource(url.toString());
            return requestURL;
        } else return url;
    }
}
