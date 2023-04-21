package org.merak.core.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpResponse extends HttpServletResponseWrapper {

    public HttpResponse(HttpServletResponse response) {
        super(response);
    }
}
