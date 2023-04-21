package control.cliente;

import control.server.RelacaoOsControl;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.bean.RelacaoOsJB;
import util.Aviso;
import util.SmartLaundryUtil;

/**
 *
 * @author webmaster
 */
public class RelacaoOsServlet extends HttpServlet {

    private RelacaoOsControl control;

    private Date dataInicio;

    private String dtInicio;

    private Date dataFinal;

    private String dtFim;

    public RelacaoOsServlet() {
        super();
        try {
            control = new RelacaoOsControl();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("-----------------------------------------------------");
        System.out.println(request.getParameter("idOS"));
        System.out.println(request.getParameter("status"));
        System.out.println("-----------------------------------------------------");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();
        Aviso aviso = null;
        SmartLaundryUtil data = new SmartLaundryUtil();
        try {
            String opcao = request.getParameter("opcao");
            if (opcao.equalsIgnoreCase("lista")) {
                dataInicio = data.stringToDate(request.getParameter("dtinicio"));
                dtInicio = data.dateToStringBD(dataInicio);
                dataFinal = data.stringToDate(request.getParameter("dtfim"));
                dtFim = data.dateToStringBD(dataFinal);
                request.getSession().setAttribute("relacaoOS", control.selectPorData(dtInicio, dtFim));
                request.getRequestDispatcher("/jsp/relacaoOs.jsp").forward(request, response);
            } else if (opcao.equalsIgnoreCase("listaCliente")) {
                dataInicio = data.stringToDate(request.getParameter("dtinicio"));
                dtInicio = data.dateToStringBD(dataInicio);
                String login = request.getParameter("login");
                dataFinal = data.stringToDate(request.getParameter("dtfim"));
                dtFim = data.dateToStringBD(dataFinal);
                request.getSession().setAttribute("relacaoCliente", control.selectCliente(dtInicio, dtFim, login));
                response.sendRedirect(request.getContextPath() + "/jsp/relacaoOsCliente.jsp");
            } else if (opcao.equals("edit")) {
                Integer id = Integer.parseInt(request.getParameter("id"));
                String status = request.getParameter("status");
                RelacaoOsJB rel = new RelacaoOsJB();
                if (status.equalsIgnoreCase("Entregue")) {
                    Date date = new Date();
                    rel.setId(id);
                    rel.setStatus(status);
                    rel.setDataSaida(date);
                    control.update2(rel);
                    request.getSession().setAttribute("relacaoOS", control.selectPorData(dtInicio, dtFim));
                    request.getRequestDispatcher("/jsp/relacaoOs.jsp").forward(request, response);
                }
                if (!status.equalsIgnoreCase("Entregue")) {
                    rel.setId(id);
                    rel.setStatus(status);
                    control.update(rel);
                    request.getSession().setAttribute("relacaoOS", control.selectPorData(dtInicio, dtFim));
                    request.getRequestDispatcher("/jsp/relacaoOs.jsp").forward(request, response);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
