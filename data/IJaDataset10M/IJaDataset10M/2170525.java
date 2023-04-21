package cat.pipo.frc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: FanServlet
 *
 */
public class ControlServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        String m = request.getParameter("m");
        if (m != null) {
            if (m.equals("start")) {
                System.out.println("Encenent ventiladors!");
                Runtime.getRuntime().exec("killall fancontrol");
            } else if (m.equals("stop")) {
                System.out.println("Aturant ventiladors!");
                Runtime.getRuntime().exec("fancontrol");
            } else if (m.equals("stat")) {
            }
        }
        request.getRequestDispatcher("jsp/status.jsp").forward(request, response);
    }
}
