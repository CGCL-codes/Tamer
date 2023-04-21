package com.sos.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sos.logical.ClienteForm;
import com.sos.logical.ClienteServices;
import com.sos.logical.PessoaServices;
import com.sos.vo.Cliente;
import com.sos.vo.Estado;
import com.sos.vo.Usuario;

public class DadosCadastraisServlet extends CustomHttpServlet {

    private static final long serialVersionUID = 1L;

    private static int[] roles = { 0 };

    public DadosCadastraisServlet() {
        super();
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!Utils.validateSession(request.getSession(), "usuario")) {
            request.getSession().invalidate();
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }
        if (!Utils.validateRoles(request.getSession(), "usuario", roles)) {
            response.sendRedirect(getServletContext().getContextPath());
            return;
        }
        ClienteServices clienteServices = ClienteServices.getInstance();
        PessoaServices pessoaServices = PessoaServices.getInstance();
        ClienteForm clienteForm = new ClienteForm(request);
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        Cliente cliente = new Cliente();
        cliente.setCodPessoa(usuario.getCodPessoa());
        try {
            cliente = clienteServices.obterCliente(cliente);
            request.setAttribute("cliente", cliente);
            request.setAttribute("estados", pessoaServices.obterEstados());
            if (request.getParameter("estado") != null) {
                Estado estado = new Estado();
                estado.setCodEstado(Integer.parseInt(request.getParameter("estado")));
                request.setAttribute("cidades", pessoaServices.obterCidades(estado));
            } else {
                request.setAttribute("cidades", pessoaServices.obterCidades(cliente.getCidade().getEstado()));
            }
            if (request.getMethod().equals("POST")) {
                clienteForm.processaForm(cliente);
                cliente.setCadastroAtivo(true);
                if (!clienteForm.hasError()) {
                    clienteServices.atualizarCliente(cliente, request.getParameter("senha"));
                    request.setAttribute("form_msg", "Dados cadastrais atualizados com sucesso!");
                    getServletContext().getRequestDispatcher("/dados_cadastrais.jsp").forward(request, response);
                    return;
                } else {
                    request.setAttribute("form_error", "Não foi possível atualizar dados cadastrais.");
                    request.setAttribute("form_error_desc", clienteForm.getMessages());
                }
            }
        } catch (Exception e) {
            request.setAttribute("form_error", e.getMessage());
        }
        getServletContext().getRequestDispatcher("/dados_cadastrais.jsp").forward(request, response);
    }
}
