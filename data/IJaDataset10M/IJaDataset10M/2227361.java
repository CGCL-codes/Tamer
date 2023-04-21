package org.impalaframework.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link HttpServletRequest} which allows underlying {@link HttpServletRequest} instance to be exposed.
 * @author Phil Zoio
 */
public interface WrapperHttpServletRequest extends HttpServletRequest {

    /**
     * Returns the first {@link HttpServletRequest} to be unwrapped which does not implement {@link WrapperHttpServletRequest}.
     */
    public HttpServletRequest getWrappedHttpServletRequest();
}
