package com.m4f.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.m4f.cityclient.api.exception.M4FCityClientException;

public class ErrorHandlerServlet extends HttpServlet {

    protected String errorPageUrl = "/error.jsp";

    public ErrorHandlerServlet() {
        super();
    }

    public ErrorHandlerServlet(String errorPageUrl) {
        this();
        this.errorPageUrl = errorPageUrl;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        M4FCityClientException exception = (M4FCityClientException) request.getAttribute("javax.servlet.error.exception");
        request.setAttribute("error_code", exception.getCod());
        request.setAttribute("error_message", exception.getMessage());
        request.setAttribute("error_description", exception.getDescription());
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(errorPageUrl);
        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
