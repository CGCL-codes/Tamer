package com.sos.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sos.logical.ClienteServices;
import com.sos.vo.Cliente;

public class VisualizarClienteServlet extends CustomHttpServlet {

    private static final long serialVersionUID = 1L;

    private static int[] roles = { 1, 2 };

    public VisualizarClienteServlet() {
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
        Cliente cliente = null;
        try {
            try {
                cliente = new Cliente();
                cliente.setCodCliente(Integer.parseInt(request.getParameter("codCliente")));
                cliente = clienteServices.obterCliente(cliente);
            } catch (NumberFormatException e) {
                cliente = null;
            }
            if (cliente == null) {
                request.setAttribute("form_error", "Cliente inexistente.");
                request.setAttribute("form_error_desc", "O cliente solicitado não existe na base de dados" + " do sistema.");
                getServletContext().getRequestDispatcher("/visualizar_cliente.jsp").forward(request, response);
                return;
            }
            request.setAttribute("cliente", cliente);
            if (request.getParameter("novo") != null) {
                request.setAttribute("form_success", "Cliente inserido com sucesso!");
            } else if (request.getParameter("editado") != null) {
                request.setAttribute("form_success", "Cliente editado com sucesso!");
            }
        } catch (Exception e) {
            request.setAttribute("form_error", "Não foi possível visualizar cliente.");
            request.setAttribute("form_error_desc", e.getMessage());
            e.printStackTrace();
        }
        getServletContext().getRequestDispatcher("/visualizar_cliente.jsp").forward(request, response);
    }
}
