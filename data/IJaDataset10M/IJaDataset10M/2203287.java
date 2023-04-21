package bg.tu_sofia.refg.imsqti.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import bg.tu_sofia.refg.imsqti.web.Qti;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sid = String.valueOf(request.getSession().getAttribute("sid"));
        Integer testSessionID = null;
        try {
            testSessionID = Integer.valueOf(request.getParameter("testSessionId"));
        } catch (Exception e) {
            log("testSessionID is NULL");
        }
        Qti service = new Qti();
        service.disconnect(sid, testSessionID);
        request.getSession().setAttribute("sid", null);
        response.sendRedirect("index.jsp");
        return;
    }
}
