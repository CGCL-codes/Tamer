package edu.asu.quadriga.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.asu.quadriga.quadruple.ElementCache;
import edu.asu.quadriga.quadruple.QuadrupleManager;

/**
 * Servlet implementation class CleanupServlet
 */
public class CleanupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CleanupServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(ServletParamter.LOGIN_IS_LOGGED_IN) == null || session.getAttribute(ServletParamter.LOGIN_IS_LOGGED_IN).equals(false) || session.getAttribute(ServletParamter.USER_IS_ADMIN) == null || session.getAttribute(ServletParamter.USER_IS_ADMIN).equals(false)) {
            RequestDispatcher view = request.getRequestDispatcher("restricted.jsp");
            view.forward(request, response);
            return;
        }
        QuadrupleManager manager = new QuadrupleManager(getServletContext(), (ElementCache) request.getSession().getAttribute(ServletParamter.SESSION_ELEMENT_CACHE));
        RequestDispatcher view = request.getRequestDispatcher("QuadrupleOverview");
        view.forward(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
