package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import business.Cheque;
import dao.ChequeDao;

public class ServletCheque extends HttpServlet implements Servlet {

    ChequeDao chequedao = new ChequeDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String comando = request.getParameter("comando");
        if (comando == null) {
            response.sendRedirect("parcelas.jsp");
        } else if (comando.equals("inserir")) {
            String codPgto = request.getParameter("codPgto");
            String multa = request.getParameter("multa");
            String numParcela = request.getParameter("numParcela");
            String valParcela = request.getParameter("valParcela");
            String status = request.getParameter("status");
            String dtVenc = request.getParameter("dtVenc");
            String tipo = request.getParameter("tipo");
            String dtPgto = request.getParameter("dtPgto");
            String numCheque = request.getParameter("numCheque");
            String codCheque = request.getParameter("codCheque");
            String banco = request.getParameter("banco");
            Cheque c1 = new Cheque();
            c1.setCodPgto(Integer.parseInt(codPgto));
            c1.setDtPgto(dtPgto);
            c1.setDtVenc(dtVenc);
            c1.setMulta(Double.parseDouble(multa));
            c1.setNumParcela(Integer.parseInt(numParcela));
            c1.setStatus(status);
            c1.setTipo(Integer.parseInt(tipo));
            c1.setValParcela(Double.parseDouble(valParcela));
            c1.setCodCheque(Integer.parseInt(codCheque));
            c1.setNumCheque(Integer.parseInt(numCheque));
            c1.setBanco(status);
            chequedao.adicionarCheque(c1);
            response.sendRedirect("chequeInserir.jsp?mensagem=cheque Inserido com Sucesso!");
        } else if (comando.equals("remover")) {
            String idParam = request.getParameter("id");
            int id = Integer.parseInt(idParam);
            chequedao.removerCheque(id);
            response.sendRedirect("chequeRemover.jsp?mensagem=cheque Removido com Sucesso!");
        } else if (comando.equals("listar")) {
            ArrayList cheques = chequedao.listarCheques();
            request.setAttribute("lista_cheques", cheques);
            RequestDispatcher rd = request.getRequestDispatcher("chequeListar.jsp");
            rd.forward(request, response);
        } else if (comando.equals("consultar")) {
            String codCheque = request.getParameter("codCheque");
            Cheque cheque = chequedao.leCheque(Integer.parseInt(codCheque));
            request.setAttribute("altera_cheque", cheque);
            RequestDispatcher rd = request.getRequestDispatcher("chequeAlterar.jsp");
            rd.forward(request, response);
        }
    }
}
