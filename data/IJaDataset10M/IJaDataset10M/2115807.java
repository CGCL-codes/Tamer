package br.ufc.quixada.adrs.comandos.visit;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Produtor;
import br.ufc.quixada.adrs.util.Msg;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ismaily
 */
public class CmdVisitEscolherProdutorRelatorio implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        List<Produtor> listProds = (List<Produtor>) session.getAttribute("produtores");
        List<Produtor> prodEncontrados = new ArrayList<Produtor>();
        String busca = request.getParameter("busca");
        if (busca == null || busca.trim().isEmpty()) {
            session.setAttribute("erro", Msg.REQUIRED_SEARCH_FIELD);
            return "/visit/visit_monta_relatorio_qt_passo_tres.jsp";
        }
        String nome = null;
        busca = busca.toUpperCase();
        for (int i = 0; i < listProds.size(); i++) {
            nome = listProds.get(i).getUsuario().getNome().toUpperCase();
            if (nome.contains(busca)) {
                prodEncontrados.add(listProds.get(i));
            }
        }
        if (prodEncontrados.isEmpty()) {
            session.setAttribute("erro", Msg.NOT_FOUND_ERROR_ADRSS);
        }
        session.setAttribute("prodEncontrados", prodEncontrados);
        return "/visit/visit_monta_relatorio_qt_passo_tres.jsp";
    }
}
