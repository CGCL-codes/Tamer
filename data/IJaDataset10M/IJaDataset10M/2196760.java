package com.googlecode.lighthttp.impl.request;

import com.googlecode.lighthttp.RequestMethod;

/**
 * Wrapper over HTTP DELETE request
 *
 * @author Sergey Prilukin
 * @version $Id: HttpDeleteWebRequest.java 41 2011-10-13 12:33:21Z sprilukin@gmail.com $
 */
public final class HttpDeleteWebRequest extends HttpGetWebRequest {

    public HttpDeleteWebRequest() {
    }

    /**
     * {@inheritDoc}
     */
    public HttpDeleteWebRequest(String url) {
        super(url);
    }

    /**
     * {@inheritDoc}
     * <br/>
     *
     * @return {@link RequestMethod#DELETE because this is implementation of HTTP DELETE request
     */
    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.DELETE;
    }
}
