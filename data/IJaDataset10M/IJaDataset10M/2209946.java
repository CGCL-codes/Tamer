package com.esri.gpt.control.filter;

import com.esri.gpt.framework.util.LogUtil;
import com.esri.gpt.framework.util.Val;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter to set the character encoding for a servlet request, 
 * and check for session timeouts.
 */
public class EncodingFilter implements Filter {

    /** Default encoding. */
    private static String ENCODING_DEFAULT = "UTF-8";

    /** The Logger. */
    private static Logger LOGGER = Logger.getLogger(EncodingFilter.class.getName());

    /** Default constructor. */
    public EncodingFilter() {
    }

    /**
   * Destroy event for the filter.
   */
    public void destroy() {
    }

    /**
   * Executes the filter.
   * <p/>
   * The character encoding for the servlet request is set to
   * UTF-8 if not already specified.
   * @param request the servlet request
   * @param response the servlet response
   * @param chain the filter chain
   * @throws IOException if an io exception occurs
   * @throws ServletException is a servlet exception occurs
   */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String homePage = "/catalog/main/home.page";
        try {
            LOGGER.finest("Entering encoding filter...");
            String sEncoding = request.getCharacterEncoding();
            if ((sEncoding == null) || (sEncoding.trim().length() == 0)) {
                request.setCharacterEncoding(ENCODING_DEFAULT);
            }
            response.setCharacterEncoding(ENCODING_DEFAULT);
            if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                HttpSession session = httpRequest.getSession(true);
                String tagName = "com.esri.gpt.control.filter.EncodingFilterTag";
                boolean hadTag = (session.getAttribute(tagName) != null);
                if (!hadTag) session.setAttribute(tagName, "tag");
                String contextPath = httpRequest.getContextPath();
                String requestURI = Val.chkStr(httpRequest.getRequestURI());
                boolean bCheck = (requestURI.indexOf("home.page") == -1) && (requestURI.indexOf("browse.page") == -1) && (requestURI.indexOf("/resource/") == -1) && (requestURI.indexOf("download.page") == -1) && (requestURI.indexOf("viewMetadataDetails.page") == -1) && !(requestURI.indexOf("report.page") >= 0 && httpRequest.getQueryString() != null && httpRequest.getQueryString().indexOf("uuid=") >= 0) && (requestURI.indexOf("preview.page") == -1) && (requestURI.indexOf("login.page") == -1);
                LOGGER.finest("check=" + bCheck + " requestURI=" + requestURI);
                if (bCheck) {
                    boolean badSession = !hadTag;
                    if (badSession) {
                        LOGGER.finest("Assuming session timeout, requestURI=" + requestURI);
                        httpResponse.sendRedirect(contextPath + homePage);
                        return;
                    }
                }
            }
        } catch (Throwable t) {
            LogUtil.getLogger().log(Level.SEVERE, "EncodingFilterException", t);
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                String contextPath = httpRequest.getContextPath();
                String requestURI = Val.chkStr(httpRequest.getRequestURI());
                LOGGER.log(Level.FINEST, "Assuming session timeout, requestURI={0}", requestURI);
                httpResponse.sendRedirect(contextPath + homePage);
            }
        }
    }

    /**
   * Initialization event for the filter
   * @param filterConfig filter configuration
   * @throws ServletException if an exception occure
   */
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
