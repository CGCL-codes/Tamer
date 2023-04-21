package org.fao.waicent.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class RequestException extends ServletException {

    String responsePage = null;

    BaseServlet servlet = null;

    HttpServletRequest request = null;

    public RequestException(BaseServlet servlet, HttpServletRequest request, String message) {
        this(servlet, request, message, null);
    }

    public RequestException(BaseServlet servlet, HttpServletRequest request, String message, String responsePage) {
        super(message);
        this.responsePage = responsePage;
        this.servlet = servlet;
        this.request = request;
        if (responsePage == null) {
            responsePage = servlet.errorPage;
        }
        request.setAttribute("message", message);
        servlet.setResponsePage(request, responsePage);
        servlet.setRequestException(request, this);
    }

    public String getResponsePage() {
        return responsePage;
    }

    public void setResponsePage(String page) {
        responsePage = page;
    }
}
