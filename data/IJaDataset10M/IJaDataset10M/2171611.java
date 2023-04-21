package com.shz.netpos.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HomeServlet
 */
public class HomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
	 * @see HttpServlet#HttpServlet()
	 */
    public HomeServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user = (String) request.getSession().getAttribute("user");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Home Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("Welcome Mr. " + user);
        out.println("<br />");
        out.println("<a href=\"LogoutServlet\">Logout</a>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
