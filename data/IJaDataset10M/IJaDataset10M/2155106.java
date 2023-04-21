package Controle.Painel;

import Entidade.Painel.Raca;
import Persistencia.Painel.RacaDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControleRacaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String operacao = request.getParameter("operacao");
        System.out.println("Controle Acionado com Operacao: " + operacao);
        String proximaPagina = "";
        if (operacao == null || operacao.equals("RacaLista")) {
            List<Raca> lstRacas = RacaDAO.getInstance().leTodos();
            request.setAttribute("lstRacas", lstRacas);
            Raca editarRacas = new Raca();
            editarRacas.setCodigo(0);
            editarRacas.setDescricao("");
            editarRacas.setEspecie("");
            request.setAttribute("editarRacas", editarRacas);
            proximaPagina = "/Painel_controle/ong/gerencia_raças.jsp";
        } else if (operacao.equals("RacaNovo")) {
            String msgErro = (String) request.getAttribute("msgErro");
            System.out.println(msgErro);
            if (msgErro == null || msgErro.equals("")) {
                Raca raca = new Raca();
                request.setAttribute("Raca", raca);
            }
            proximaPagina = "/Painel_controle/ong/gerencia_raças.jsp";
        } else if (operacao.equals("RacaNovoProcessa")) {
            Raca raca = new Raca();
            raca.setDescricao(request.getParameter("descricao").trim());
            String especie = request.getParameter("cao");
            if (especie != "on") {
                especie = "Cao";
            } else {
                especie = "Gato";
            }
            raca.setEspecie(especie);
            String msgErro = raca.validaDados(raca.INCLUSAO);
            if (msgErro.equals("")) {
                raca.setDescricao(raca.getDescricao());
                raca.setEspecie(raca.getEspecie());
                RacaDAO.getInstance().grava(raca);
                request.removeAttribute("msgErro");
                request.removeAttribute("Raca");
                proximaPagina = "ControleRacaServlet?operacao=RacaLista";
            } else {
                request.setAttribute("msgErro", msgErro);
                request.setAttribute("Raca", raca);
                proximaPagina = "ControleRacaServlet?operacao=RacaNovo";
            }
        } else if (operacao.equals("RacaMostra")) {
            int codigo = Integer.parseInt(request.getParameter("codigo"));
            Raca raca = RacaDAO.getInstance().le(codigo);
            request.setAttribute("Raca", raca);
            proximaPagina = "/Painel_controle/ong/gerencia_raças.jsp";
        } else if (operacao.equals("RacaEdita")) {
            String msgErro = (String) request.getAttribute("msgErro");
            if (msgErro == null || msgErro.equals("")) {
                int codigo = Integer.parseInt(request.getParameter("codigo"));
                Raca raca = RacaDAO.getInstance().le(codigo);
                List<Raca> lstRacas = RacaDAO.getInstance().leTodos();
                Raca editarRacas = new Raca();
                editarRacas.setCodigo(raca.getCodigo());
                editarRacas.setDescricao(raca.getDescricao());
                editarRacas.setEspecie(raca.getEspecie());
                request.setAttribute("lstRacas", lstRacas);
                request.setAttribute("editarRacas", editarRacas);
            }
            proximaPagina = "/Painel_controle/ong/gerencia_raças.jsp";
        } else if (operacao.equals("RacaEditaProcessa")) {
            Raca Raca = new Raca();
            int codigo = Integer.parseInt(request.getParameter("codigo"));
            Raca.setCodigo(codigo);
            Raca.setDescricao(request.getParameter("descricao").trim());
            String especie = request.getParameter("cao");
            if (especie != "on") {
                especie = "Cao";
            } else {
                especie = "Gato";
            }
            Raca.setEspecie(especie);
            String msgErro = Raca.validaDados(Raca.ALTERACAO);
            if (msgErro.equals("")) {
                Raca raca = new Raca();
                raca.setCodigo(Raca.getCodigo());
                raca.setDescricao(Raca.getDescricao());
                raca.setEspecie(Raca.getEspecie());
                RacaDAO.getInstance().altera(raca);
                request.removeAttribute("msgErro");
                request.removeAttribute("Raca");
                proximaPagina = "ControleRacaServlet?operacao=RacaLista";
            } else {
                request.setAttribute("msgErro", msgErro);
                request.setAttribute("Raca", Raca);
                proximaPagina = "ControleRacaServlet?operacao=RacaEdita";
            }
        } else if (operacao.equals("RacaApaga")) {
            int codigo = Integer.parseInt(request.getParameter("codigo"));
            int ret = RacaDAO.getInstance().apaga(codigo);
            proximaPagina = "ControleRacaServlet?operacao=RacaLista";
        }
        RequestDispatcher rd = request.getRequestDispatcher(proximaPagina);
        rd.forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
