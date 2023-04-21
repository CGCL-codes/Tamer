package com.release.ldap.helper;

import java.io.IOException;
import java.util.Hashtable;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.GenericServlet;
import javax.servlet.*;

public class LoginServlet extends HttpServlet {

    public static int USERID = -1;

    /**
	 * Services the client request to login.  
	 */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            System.out.println("log ser  conn" + new java.util.Date());
            String userName = request.getParameter("UserName");
            String password = request.getParameter("Password");
            session.setAttribute("temo", " Hii");
            String errorMsg = "";
            session.setAttribute("SESSION_ID", session.getId());
            session.setAttribute("USER_ID", "xyz@cgi.com");
            session.setAttribute("USER_GROUP_NAME", "Administrator");
            session.setAttribute("errorMsg", errorMsg);
            RequestDispatcher rd = request.getRequestDispatcher("mainpage.jsp");
            rd.forward(request, response);
        } catch (Exception le) {
            le.printStackTrace();
            request.setAttribute("Login", "Failed");
            response.sendRedirect("ErrorLogin.jsp");
        }
    }
}
