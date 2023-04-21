package com.javaeedev.lightweight.mvc;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Wrap all servlet's objects and can be retrieved via thread local.
 * 
 * @author Xuefeng
 */
public final class ActionContext {

    private static ThreadLocal<ActionContext> contextThreadLocal = new ThreadLocal<ActionContext>();

    /**
     * Get current ActionContext.
     * 
     * @return ActionContext.
     */
    public static ActionContext getActionContext() {
        return contextThreadLocal.get();
    }

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private ServletContext context;

    /**
     * Initiate all servlet objects as thread local.
     * 
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @param session HttpSession object.
     * @param context ServletContext object.
     */
    static void setActionContext(HttpServletRequest request, HttpServletResponse response, HttpSession session, ServletContext context) {
        ActionContext actionContext = new ActionContext();
        actionContext.setRequest(request);
        actionContext.setResponse(response);
        actionContext.setSession(session);
        actionContext.setServletContext(context);
        contextThreadLocal.set(actionContext);
    }

    /**
     * Remove all servlet objects from thread local.
     */
    static void remove() {
        contextThreadLocal.remove();
    }

    /**
     * Get HttpServletRequest object.
     * 
     * @return HttpServletRequest object.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Set HttpServletRequest object.
     * 
     * @param request HttpServletRequest object.
     */
    void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Get HttpServletResponse object.
     * 
     * @return HttpServletResponse object.
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Set HttpServletResponse object.
     * 
     * @param response HttpServletResponse object.
     */
    void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Get HttpSession object.
     * 
     * @return HttpSession object.
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * Set HttpSession object.
     * 
     * @param session HttpSession object.
     */
    void setSession(HttpSession session) {
        this.session = session;
    }

    /**
     * Get ServletContext object.
     * 
     * @return ServletContext object.
     */
    public ServletContext getServletContext() {
        return context;
    }

    /**
     * Set ServletContext object.
     * 
     * @param context ServletContext object.
     */
    void setServletContext(ServletContext context) {
        this.context = context;
    }
}
