package es.cea.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.cea.modelo.Libro;

/**
 * Servlet implementation class ControladorOpcion1
 */
public class ControladorOpcion3 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Autor autor = new Autor("mi autor");
        Libro libro = new Libro("mi libro");
        request.setAttribute("modelo", libro);
        request.setAttribute("autor", autor);
        request.getSession().setAttribute("usuario", "PEPE");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/opcion3.jsp");
        requestDispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
