package com.cokemi.utils.mvc.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * һ������������Action�ɷ���(��Strutsʵ��)
 * @author Jammy Zhou
 *
 */
@SuppressWarnings("serial")
public class ActionDispatcher extends HttpServlet {

    private static String actionPackage = null;

    public void init() throws ServletException {
        if (actionPackage == null) {
            actionPackage = this.getInitParameter("actionPackage") + ".";
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String forwardPage = "";
        try {
            String actionClass = getActionClassName(request);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            TemplateAction action = (TemplateAction) classLoader.loadClass(actionPackage + actionClass).newInstance();
            forwardPage = action.doOperation(request, response, this.getServletContext());
            if (forwardPage != null && forwardPage.length() > 0) {
                request.getRequestDispatcher(forwardPage).forward(request, response);
            }
        } catch (Exception e) {
            try {
                response.setContentType("text/html; charset=GBK");
                response.getWriter().println("<pre>");
                if (e.getCause() != null) {
                    e.getCause().printStackTrace(response.getWriter());
                } else {
                    e.printStackTrace(response.getWriter());
                }
                response.getWriter().println("</pre>");
            } catch (Exception ex) {
                ;
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }

    protected String getActionClassName(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(uri.lastIndexOf("/") + 1);
        if (uri.indexOf(".") > 0) {
            uri = uri.substring(0, uri.indexOf("."));
        }
        uri = uri.substring(0, 1).toUpperCase() + uri.substring(1);
        return getActionClassPrefix() + uri + getActionClassSuffix();
    }

    protected String getActionClassPrefix() {
        return "";
    }

    protected String getActionClassSuffix() {
        return "Action";
    }
}
