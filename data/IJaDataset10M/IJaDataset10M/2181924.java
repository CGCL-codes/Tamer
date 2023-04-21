package com.esri.gpt.control.sitemap;

import com.esri.gpt.framework.context.BaseServlet;
import com.esri.gpt.framework.context.RequestContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Endpoint for handling sitemap related requests.
 */
public class SitemapServlet extends BaseServlet {

    /**
   * Processes the HTTP request.
   * @param request the HTTP request
   * @param response the HTTP response
   * @param context the request context
   * @throws Exception if an exception occurs
   */
    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response, RequestContext context) throws Exception {
        SitemapHandler handler = new SitemapHandler();
        handler.handle(request, response, context);
    }
}
