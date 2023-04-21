package de.uni_hamburg.golem.service;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: CASDemo
 * 
 * Mirrors the CAS "SimpleTestUsernamePassword"
 * Authentication handler behaviour. 
 *
 * @web.servlet
 *   name="CASDemo"
 *   display-name="CASDemo" 
 *
 * @web.servlet-mapping
 *   url-pattern="/caslogin"
 *  
 */
public class CASDemo extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public static final String PASSWORD = "password";

    public static final String USERID = "userid";

    public CASDemo() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        String userid = request.getParameter(USERID);
        String password = request.getParameter(PASSWORD);
        int result = 0;
        log("LOGIN: <" + userid + "," + password + ">");
        if (userid == null || password == null) {
            result = -1;
        } else {
            if (userid.equalsIgnoreCase(password)) {
                result = 1;
            } else {
                result = 0;
            }
        }
        out.println("" + result);
        out.close();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        log("Initializing " + this.getClass());
    }
}
