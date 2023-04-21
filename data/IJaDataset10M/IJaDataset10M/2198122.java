package controller;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author MAN
 */
public class CerrarSesionAction extends Action implements Serializable {

    String next = "";

    HttpSession session = null;

    @Override
    public void run() throws ServletException, IOException {
        System.out.println("hola mundo");
        session = request.getSession();
        Object obj = session.getAttribute("ident");
        String n = (String) obj;
        System.out.println(n);
        session.invalidate();
        next = "/index.jsp";
        RequestDispatcher rd = application.getRequestDispatcher(next);
        if (rd == null) throw new ServletException("No se pudo encontrar " + next);
        rd.forward(request, response);
    }
}
