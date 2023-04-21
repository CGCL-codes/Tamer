package br.ufc.quixada.adrs.comandos.visit;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.util.Msg;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Welligton
 */
public class CmdVisitMontaRelatorioQLPassoQuatro implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession sessao = request.getSession(true);
        List<String> questoesMarcadas = new ArrayList<String>();
        boolean cb2 = request.getParameter("cb") != null ? true : false;
        if (cb2) {
            String cb[] = request.getParameterValues("cb");
            for (int i = 0; i < cb.length; i++) {
                questoesMarcadas.add(cb[i]);
            }
        } else {
            sessao.setAttribute("erro", Msg.EMPTY_SELECTION_ERROR_QUESTION);
            return "/visit/visit_monta_relatorio_ql_passo_quatro.jsp";
        }
        sessao.setAttribute("questoesMarcadas", questoesMarcadas);
        return "/visit/visit_monta_relatorio_ql_passo_cinco.jsp";
    }
}
