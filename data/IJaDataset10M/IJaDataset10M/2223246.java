package edu.asu.wordpower.users.servlets;

import java.io.IOException;
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
import edu.asu.wordpower.users.User;
import edu.asu.wordpower.users.UsersManager;

/**
 * Servlet implementation class ManageUsers
 */
public class ManageUsers extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageUsers() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.getAttribute(Parameter.IS_LOGGED_IN) == null || session.getAttribute(Parameter.IS_LOGGED_IN).equals(false) || session.getAttribute(Parameter.IS_ADMIN) == null || session.getAttribute(Parameter.IS_ADMIN).equals(false)) {
            RequestDispatcher view = request.getRequestDispatcher("login.jsp");
            view.forward(request, response);
            return;
        }
        DatabaseProvider provider = (DatabaseProvider) getServletContext().getAttribute(Parameter.DB_PROVIDER_CONTEXT_PARAMETER);
        DatabaseManager manager = provider.getDatabaseManager(DBNames.USER_DB);
        UsersManager usersManager = new UsersManager(manager);
        User[] users = usersManager.getAllUsers();
        request.setAttribute("users", users);
        RequestDispatcher view = request.getRequestDispatcher("userAdmin.jsp");
        view.forward(request, response);
        usersManager.close();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
