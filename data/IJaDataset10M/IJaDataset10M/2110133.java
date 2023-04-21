package servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import Utils.Project;
import Utils.UserInfo;
import Engine.DetectEngine;

public class CreateProjectReport extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Constructor of the object.
	 */
    public CreateProjectReport() {
        super();
    }

    /**
	 * Destruction of the servlet. <br>
	 */
    public void destroy() {
        super.destroy();
    }

    /**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");
        String project = request.getParameter("project");
        DetectEngine engine = new DetectEngine();
        engine.reportFromProject(project, userInfo);
        HttpSession session = request.getSession();
        session.setAttribute("reports", engine.getReports());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/showReports.jsp");
        dispatcher.forward(request, response);
    }

    /**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
    public void init() throws ServletException {
    }
}
