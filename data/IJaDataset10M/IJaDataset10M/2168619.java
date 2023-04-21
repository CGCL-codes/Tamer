package edu.asu.wordpower.users.servlets;

import java.io.IOException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.asu.wordpower.db4o.DBNames;
import edu.asu.wordpower.db4o.DatabaseManager;
import edu.asu.wordpower.db4o.DatabaseProvider;
import edu.asu.wordpower.servlet.Parameter;
import edu.asu.wordpower.users.UsersManager;

/**
 * Servlet implementation class DeleteUserServlet
 */
public class DeleteUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUserServlet() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(Parameter.IS_LOGGED_IN) == null || session.getAttribute(Parameter.IS_LOGGED_IN).equals(false) || session.getAttribute(Parameter.IS_ADMIN) == null || session.getAttribute(Parameter.IS_ADMIN).equals(false)) {
            RequestDispatcher view = request.getRequestDispatcher("restricted.jsp");
            view.forward(request, response);
            return;
        }
        String username = request.getParameter("user");
        DatabaseProvider provider = (DatabaseProvider) getServletContext().getAttribute(Parameter.DB_PROVIDER_CONTEXT_PARAMETER);
        DatabaseManager manager = provider.getDatabaseManager(DBNames.USER_DB);
        UsersManager usersManager = new UsersManager(manager);
        Map<String, String> admins = (Map<String, String>) getServletContext().getAttribute(Parameter.USER_MAP);
        usersManager.setAdmins(admins);
        usersManager.deleteUser(username);
        RequestDispatcher view = request.getRequestDispatcher("ManageUsers");
        view.forward(request, response);
        usersManager.close();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
