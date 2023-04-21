package br.com.efitness.web.professor;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.com.efitness.Aluno;
import br.com.efitness.web.GenericR2D2Servlet;
import br.com.efitness.web.helper.OrdenarUsuariosHelper;

public class OrdenarAlunosServlet extends GenericR2D2Servlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrdenarUsuariosHelper<Aluno> helper = new OrdenarUsuariosHelper<Aluno>(this.getRepositorio(), request);
        helper.setNomeAtributo("alunos");
        helper.doHelp();
        this.foward(request, response, "/professor/aluno/listar.jsp");
    }
}
