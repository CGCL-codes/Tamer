package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class servicios extends HttpServlet {

    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operacion = request.getParameter("servicio");
        HttpSession session = request.getSession();
        if (operacion != null && request.getSession().getAttribute("DM").equals("laptop")) {
            session.setAttribute("DESPLIEGE_SERVICIO", operacion);
            response.sendRedirect("/PICM/laptop/servicios.jsp");
        }
        if (operacion != null && request.getSession().getAttribute("DM").equals("iphone")) {
            session.setAttribute("DESPLIEGE_SERVICIO", operacion);
            response.sendRedirect("/PICM/iphone/servicios.jsp");
        }
        if (operacion != null && request.getSession().getAttribute("DM").equals("blackberry8320")) {
            System.out.println("operacion " + operacion);
            session.setAttribute("DESPLIEGE_SERVICIO", operacion);
            response.sendRedirect("/PICM/blackberry8320/servicios.jsp");
        }
    }
}
