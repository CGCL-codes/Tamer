package servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import business.Venda;
import dao.VendaDao;

public class ServletVenda extends HttpServlet implements Servlet {

    VendaDao vendadao = new VendaDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String comando = request.getParameter("comando");
        if (comando == null) {
            response.sendRedirect("venda.jsp");
        } else if (comando.equals("inserir")) {
            String codVenda = request.getParameter("codVenda");
            String matVendedor = request.getParameter("matVendedor");
            String desconto = request.getParameter("desconto");
            String valorTotal = request.getParameter("valorTotal");
            Venda v1 = new Venda();
            v1.setCodVenda(Integer.parseInt(codVenda));
            v1.setMatVendedor(Integer.parseInt(matVendedor));
            v1.setDesconto(Double.parseDouble(desconto));
            v1.setValorTotal(Double.parseDouble(valorTotal));
            vendadao.adicionarVenda(v1);
            response.sendRedirect("vendaInserir.jsp?mensagem=venda Inserida com Sucesso!");
        } else if (comando.equals("remover")) {
            String codVenda = request.getParameter("codVenda");
            vendadao.removerVenda(Integer.parseInt(codVenda));
            response.sendRedirect("vendaRemover.jsp?mensagem=venda Removida com Sucesso!");
        } else if (comando.equals("listar")) {
            ArrayList vendas = vendadao.listarVendas();
            request.setAttribute("lista_venda", vendas);
            RequestDispatcher rd = request.getRequestDispatcher("vendaListar.jsp");
            rd.forward(request, response);
        } else if (comando.equals("consultar")) {
            String codVenda = request.getParameter("codVenda");
            Venda venda = vendadao.leVenda(Integer.parseInt(codVenda));
            request.setAttribute("altera_venda", venda);
            RequestDispatcher rd = request.getRequestDispatcher("vendaAlterar.jsp");
            rd.forward(request, response);
        }
    }
}
