package edu.asu.quadriga.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(ServletParamter.LOGIN_IS_LOGGED_IN) != null && session.getAttribute(ServletParamter.LOGIN_IS_LOGGED_IN).equals(true)) {
            session.setAttribute(ServletParamter.LOGIN_IS_LOGGED_IN, false);
            session.setAttribute(ServletParamter.USER_IS_ADMIN, false);
            String user = session.getAttribute(ServletParamter.LOGIN_USERNAME).toString();
            session.setAttribute(ServletParamter.LOGIN_USERNAME, null);
            synchronized (session) {
                session.invalidate();
            }
            log("Logged out " + user);
        }
        RequestDispatcher view = request.getRequestDispatcher("loggedOut.jsp");
        view.forward(request, response);
        return;
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
