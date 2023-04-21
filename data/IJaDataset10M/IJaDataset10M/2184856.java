package net.lucianodacunha.learningjee.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: GetParametersServlet
 *
 */
public class GetParametersServlet extends PrimeiroServletEclipse {

    public GetParametersServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }

    @Override
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doProcess(request, response);
        String[] parameters = request.getParameterValues("musica");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>Estilos escolhidos: </h3>");
        for (String parameter : parameters) {
            out.println("<h4>" + parameter + ";</h4>");
        }
        out.println("<br />");
        out.println("<br />");
        out.println("<a href='form_musicas.html'>Voltar para o formul�rio</a>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
