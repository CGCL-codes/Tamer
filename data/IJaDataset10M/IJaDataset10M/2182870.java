package com.kizna.servletunit.examples;

import java.io.*;
import javax.servlet.*;
import java.io.IOException;
import javax.servlet.http.*;

/**
 * Insert the type's description here.
 * Creation date: (5/6/2001 5:23:37 PM)
 * @author: 
 */
public class HelloUser extends javax.servlet.http.HttpServlet {

    /**
	 * HelloUser constructor comment.
	 */
    public HelloUser() {
        super();
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (5/6/2001 5:24:12 PM)
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String userName = request.getParameter("UserName");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello " + userName + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Hello " + userName + "!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
