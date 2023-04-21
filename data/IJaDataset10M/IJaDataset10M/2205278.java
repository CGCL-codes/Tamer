package net.lucianodacunha.learningjee.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FechandoSessoesServlet
 */
public class FechandoSessoesServlet extends PrimeiroServletEclipse {

    private static final long serialVersionUID = 1L;

    /**
     * @see PrimeiroServletEclipse#PrimeiroServletEclipse()
     */
    public FechandoSessoesServlet() {
        super();
    }

    @Override
    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doProcess(request, response);
        HttpSession sessao = request.getSession();
        sessao.removeAttribute("name");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Fechando um sess�o</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h3>...neste ponto sua sess�o j� deve ter sido fechada!</h3>");
        out.println("<h3>ID da sess�o criada: " + sessao.getId() + "</h3>");
        out.println("<h3>Valor do atributo name ap�s removido: " + sessao.getAttribute("name") + "</h3>");
        out.println("<a href=\"CriandoSessoesServlet\">Criar outra sess�o</a><br/><br/>");
        out.println("<a href='index.html'>Voltar para o formul�rio</a>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
