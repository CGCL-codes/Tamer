package ensino2.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.ufpr.biblioteca.bean.UsuarioBean;
import br.ufpr.biblioteca.facade.FacadeBiblioteca;
import ensino2.Desempenho;
import ensino2.Disciplina;
import ensino2.comum.IClassControl;
import ensino2.comum.MsgPage;
import ensino2.dao.DesempenhoDao;
import ensino2.dao.DisciplinaDao;

public class DesempenhoControl implements IClassControl {

    public static final String PARAM_NAME_COD_ATIVIDADE = "c_atv";

    public static final String PARAM_NAME_TITULO = "atv_tit";

    public static final String PARAM_NAME_ANO = "des_ano";

    public void redireciona(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String url = "./desempenho.jsp";
        try {
            Disciplina dis = DisciplinaDao.pesquisar(Integer.parseInt(req.getParameter(DisciplinaControl.PARAM_NAME_COD_DISCIPLINA)), req);
            req.setAttribute("Disciplina", dis);
            req.setAttribute("desAno", req.getParameter(PARAM_NAME_ANO));
            if (req.getParameter(ControleGeral.SUBMIT).equals("Desempenho dos Alunos")) req.setAttribute("desempenho", formDesempenho(req, dis));
            if (req.getParameter(ControleGeral.SUBMIT).equals("Salvar")) url = salvar(req);
            if (req.getParameter(ControleGeral.SUBMIT).equals("Nao") || req.getParameter(ControleGeral.SUBMIT).equals("Voltar")) resp.sendRedirect("./index.jsp");
        } catch (NullPointerException e) {
            resp.sendRedirect("./index.jsp");
        } catch (SQLException e) {
            resp.sendRedirect("./index.jsp");
        }
        RequestDispatcher red = req.getRequestDispatcher(url);
        red.include(req, resp);
    }

    private static String formDesempenho(HttpServletRequest req, Disciplina dis) {
        String aux = "";
        int i = 0;
        try {
            ArrayList<Desempenho> colDes = DesempenhoDao.carregarDesempenho(dis, Integer.parseInt(req.getParameter(PARAM_NAME_ANO)));
            System.out.println(colDes.size());
            int j = 1;
            for (Iterator<Desempenho> it = colDes.iterator(); it.hasNext(); ) {
                i++;
                if (j == 0) {
                    j = 1;
                } else {
                    j = 0;
                }
                Desempenho des = it.next();
                UsuarioBean ub = new FacadeBiblioteca().getUsuarioBean(des.getCodAluno());
                String frm = "<tr class=\"st" + j + "\"><td width=\"5px\">" + i + "</td><td><b>" + ub.getNome() + "</b></td><td width=\"25px\"><input name=\"nota_" + des.getCodAluno() + "\" type=\"text\" size=\"5\" value=\"" + des.getNota() + "\"/></td><td width=\"15px\">" + "<input name=\"falta_" + des.getCodAluno() + "\" type=\"text\" size=\"8\" value=\"" + des.getFalta() + "\"/></td></tr>" + "\n";
                aux += frm;
            }
        } catch (Exception e) {
        }
        return aux;
    }

    @SuppressWarnings("unchecked")
    public static String salvar(HttpServletRequest req) {
        try {
            int codDis = Integer.parseInt(req.getParameter(DisciplinaControl.PARAM_NAME_COD_DISCIPLINA));
            Desempenho des;
            int nota, falta;
            int codAln;
            Enumeration<String> parametros = req.getParameterNames();
            while (parametros.hasMoreElements()) {
                String aux = parametros.nextElement();
                if (aux.indexOf("nota") >= 0) {
                    codAln = Integer.parseInt(aux.substring(aux.lastIndexOf("_") + 1, aux.length()));
                    try {
                        nota = Integer.parseInt(req.getParameter(aux));
                        falta = Integer.parseInt(req.getParameter("falta_" + codAln));
                    } catch (Exception e) {
                        nota = 0;
                        falta = 0;
                    }
                    des = new Desempenho(codAln, codDis, new GregorianCalendar().get(GregorianCalendar.YEAR), falta, nota, System.currentTimeMillis());
                    DesempenhoDao.salvar(des);
                }
            }
            return MsgPage.exibeMsg("./index.jsp", false, req, "Desempenho Salvo Com Sucesso!!!", "");
        } catch (Exception e) {
            return MsgPage.exibeMsg("./index.jsp", true, req, "Erro ao Salvar Desempenho!!!", "");
        }
    }
}
