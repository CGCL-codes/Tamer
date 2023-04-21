package com.strategicgains.openef.web;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Todd Fredrich
 * @since May 27, 2005
 * @version $Revision: 1.1 $
 */
public class DefaultServletAdapterFactory implements ServletAdapterFactory {

    private static final DefaultServletAdapterFactory INSTANCE = new DefaultServletAdapterFactory();

    private DefaultServletAdapterFactory() {
        super();
    }

    public static DefaultServletAdapterFactory getInstance() {
        return INSTANCE;
    }

    public HttpServletRequestAdapter newHttpServletRequestAdapter(HttpServletRequest request) {
        return new HttpServletRequestAdapter(request);
    }

    public ApplicationSessionState newApplicationSessionState() {
        return new ApplicationSessionState();
    }
}
