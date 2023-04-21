package bioroot;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 Logout page, kills session, frees objects 
 */
public class Logout extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        RequestDispatcher dispatcher = request.getRequestDispatcher("Login");
        dispatcher.forward(request, response);
    }
}
