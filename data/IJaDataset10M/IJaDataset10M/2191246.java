package br.com.efitness.web.professor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.validator.GenericValidator;
import br.com.efitness.Aluno;
import br.com.efitness.AvaliacaoFisica;
import br.com.efitness.web.GenericR2D2Servlet;

public class BuscarAlunoServlet extends GenericR2D2Servlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getParameter("id");
        if (sId != null && !sId.trim().isEmpty() && GenericValidator.isLong(sId)) {
            Long id = Long.parseLong(sId);
            Aluno aluno = this.getRepositorio().get(id, Aluno.class);
            request.setAttribute("aluno", aluno);
            request.setAttribute("referer", request.getHeader("Referer"));
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("alunoId", id);
            List<AvaliacaoFisica> avaliacoes = this.getRepositorio().executarBusca("buscar.ultima.avaliacaoFisica", parametros, 0, 1);
            request.setAttribute("avaliacaoFisica", avaliacoes.get(0));
            this.foward(request, response, "/professor/aluno/exibir.jsp");
        } else {
            this.foward(request, response, "/professor/index.jsp");
        }
    }
}
