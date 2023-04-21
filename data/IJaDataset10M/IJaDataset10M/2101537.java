package org.nomicron.suber.rest;

import org.nomicron.suber.model.factory.MetaFactoryAwareBase;
import javax.servlet.http.HttpServletRequest;

/**
 * Base RestUrlAdapter for others to extend.
 */
public abstract class BaseUrlAdapter extends MetaFactoryAwareBase implements RestUrlAdapter {

    private String redirectUrl;

    private String notFoundUrl;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getNotFoundUrl() {
        return notFoundUrl;
    }

    public void setNotFoundUrl(String notFoundUrl) {
        this.notFoundUrl = notFoundUrl;
    }

    /**
     * Get the last part of the url (after the final /), normally the identifier.
     *
     * @param request HttpServletRequest
     * @return last section of the url
     */
    protected String lastUrlSection(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String[] splitUrl = requestUrl.split("/");
        return splitUrl[splitUrl.length - 1];
    }
}
