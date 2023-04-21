package com.deitel.advjhtp1.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class WelcomeServlet3 extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstname");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<?xml version = \"1.0\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD " + "XHTML 1.0 Strict//EN\" \"http://www.w3.org" + "/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        out.println("<html xmlns = \"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<title>Processing post requests with data</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Hello " + firstName + ",<br />");
        out.println("Welcome to Servlets!</h1>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
