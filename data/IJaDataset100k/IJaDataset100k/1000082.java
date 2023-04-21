package chat;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        if (req.getParameter("usuario") != null) {
            req.getSession().setAttribute("usuario", req.getParameter("usuario"));
        }
        if (req.getSession().getAttribute("usuario") == null) {
            pw.println("<form action=\"/chat/RegistroServlet\" method=\"post\">");
            pw.println("Usuario: <input type=\"text\" name=\"usuario\"><br>");
            pw.println("Clave: <input type=\"text\" name=\"clave\"><br>");
            pw.println("<input type=\"submit\" \"value=\"Registrar\"> </form>");
        } else {
            pw.println("Ya esta registrado");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
