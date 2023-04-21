package br.ufpe.ankos.model.web.tiposAmbientes;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.ufpe.ankos.model.fachadas.FachadaAnkos;
import br.ufpe.ankos.model.tiposAmbiente.TipoAmbiente;

/**
 * Servlet implementation class for Servlet: ServletCadastrarTipoAmbiente.
 * 
 */
public class ServletCadastrarTipoAmbiente extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    private FachadaAnkos fachadaAnkos;

    public ServletCadastrarTipoAmbiente() {
        super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensagemRetorno = "";
        String linkRetorno = "../../../../../../jsp/tiposAmbientes/cadastrarTipoAmbiente.jsp";
        TipoAmbiente tipoAmbiente = new TipoAmbiente();
        try {
            String descricao = request.getParameter("descricao");
            tipoAmbiente.setDescricao(descricao);
            fachadaAnkos.cadastrarTipoAmbiente(tipoAmbiente);
            mensagemRetorno = "Tipo de Ambiente cadastrado com sucesso!";
        } catch (Exception e) {
            mensagemRetorno = e.getMessage();
        }
        response.sendRedirect(linkRetorno + "?mensagem=" + mensagemRetorno);
    }

    public void init() throws ServletException {
        fachadaAnkos = FachadaAnkos.getInstance();
    }
}
