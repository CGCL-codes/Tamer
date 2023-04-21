package bg.tu_sofia.refg.imsqti.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import bg.tu_sofia.refg.imsqti.web.Qti;
import bg.tu_sofia.refg.imsqti.web.QtiServiceException;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Qti service = new Qti();
        String userName = request.getParameter("user");
        if (userName == null || userName.isEmpty()) {
            response.sendRedirect("login.jsp?error=noUserName");
            return;
        }
        String password = request.getParameter("pass");
        if (password == null || password.isEmpty()) {
            response.sendRedirect("login.jsp?error=noPass");
            return;
        }
        HttpSession session = request.getSession();
        String sid;
        try {
            sid = service.authenticate(userName, password);
        } catch (QtiServiceException e) {
            response.sendRedirect("login.jsp?error=incorrect");
            return;
        }
        session.setAttribute("sid", sid);
        session.setAttribute("user", userName);
        response.sendRedirect("index.jsp");
        return;
    }
}
