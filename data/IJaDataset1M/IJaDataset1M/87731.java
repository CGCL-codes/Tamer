package com.sos.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sos.logical.ClienteServices;
import com.sos.logical.OrdemServicoServices;
import com.sos.vo.Cliente;
import com.sos.vo.OrdemServico;
import com.sos.vo.Usuario;

public class ListarOrdensServicoServlet extends CustomHttpServlet {

    private static final long serialVersionUID = 1L;

    private static int[] roles = { 0, 1, 2 };

    public ListarOrdensServicoServlet() {
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
        OrdemServicoServices ordemServicoServices = OrdemServicoServices.getInstance();
        ClienteServices clienteServices = ClienteServices.getInstance();
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        List<OrdemServico> ordensServico;
        try {
            if (usuario.getNivel() == 0) {
                Cliente cliente = new Cliente();
                cliente.setCodPessoa(usuario.getCodPessoa());
                cliente = clienteServices.obterCliente(cliente);
                ordensServico = ordemServicoServices.obterOrdensServico(cliente);
            } else if (request.getParameter("codCliente") != null) {
                Cliente cliente = new Cliente();
                cliente.setCodPessoa(Integer.parseInt(request.getParameter("codCliente")));
                cliente = clienteServices.obterCliente(cliente);
                ordensServico = ordemServicoServices.obterOrdensServico(cliente);
            } else {
                ordensServico = ordemServicoServices.obterOrdensServico(null);
            }
            request.setAttribute("ordensServico", ordensServico);
            request.setAttribute("numOrdensServico", ordensServico.size());
        } catch (Exception e) {
            request.setAttribute("form_error", "Não foi possível listar ordens de serviço.");
            request.setAttribute("form_error_desc", e.getMessage());
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/listar_os.jsp").forward(request, response);
    }
}
