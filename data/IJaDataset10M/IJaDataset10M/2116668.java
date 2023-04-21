package by.brsu.portal.servlets;

import java.io.IOException;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Factory factory = new Factory();

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected String getActionName(HttpServletRequest request) {
        String actionName = request.getRequestURI().substring(request.getContextPath().length());
        if (actionName != null && !"".equals(actionName) && !"/".equals(actionName)) return actionName.substring(actionName.lastIndexOf("/") + 1, actionName.length());
        return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Action action = factory.create(getActionName(request), request);
        if (action.perform(request, response)) {
            for (Entry<String, Object> entry : action.getParametersMap().entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        String url = factory.getForwardURL();
        if (url != null) getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
