package org.fantasy.common.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *  当前线程上下文
 * @author: 王文成
 * @version: 1.0
 * @since 2010-5-12
 */
public class WebContext {

    /**
     * Constant for the HTTP request object.
     */
    public static final String HTTP_REQUEST = "HTTP_REQUEST";

    /**
     * Constant for the HTTP response object.
     */
    public static final String HTTP_RESPONSE = "HTTP_RESPONSE";

    /**
     * Web容器上下文
     */
    private static ServletContext servletContext;

    /**
     * 取得 HttpServletRequest
     * 考虑了过滤器加载顺序问题,如果Strust2的过滤器优先执行,ThreadMap.get(HTTP_REQUEST)将取不到request。
     * 
     * @return
     */
    public static HttpServletRequest getRequest() {
        Object request = ThreadMap.get(HTTP_REQUEST);
        return (HttpServletRequest) request;
    }

    /**
     * 取得 HttpServletResponse
     * 考虑了过滤器加载顺序问题,如果Strust2的过滤器优先执行,ThreadMap.get(HTTP_RESPONSE)将取不到response。
     * 
     * @return
     */
    public static HttpServletResponse getResponse() {
        Object response = ThreadMap.get(HTTP_RESPONSE);
        return (HttpServletResponse) response;
    }

    /**
     * 取得 HttpSession
     * 
     * @return
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 取得 ServletContext
     * 
     * @return
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 取得 ServletContext
     * 
     * @return
     */
    public static void setServletContext(ServletContext context) {
        servletContext = context;
    }

    /**
     * 取得 SpringBean
     * 
     * @return
     */
    public static Object getBean(String id) {
        return WebUtil.getBean(getServletContext(), id);
    }
}
