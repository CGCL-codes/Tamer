package br.ufc.quixada.adrs.comandos.visit;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Prazo;
import br.ufc.quixada.adrs.service.AdrsService;
import br.ufc.quixada.adrs.service.PrazoService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Balthazar
 */
public class CmdVisitListarConfirmacoes implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long id = Long.parseLong(request.getParameter("prazo_id"));
        Prazo prazoAtual = new PrazoService().getPrazoById(id);
        AdrsService as = new AdrsService();
        session.setAttribute("naoConfirmados", as.getAdrsNaoConfirmadosByPrazo(prazoAtual));
        session.setAttribute("confirmados", as.getAdrsConfirmadosByPrazo(prazoAtual));
        session.setAttribute("prazoAtual", prazoAtual);
        return "/visit/visit_ger_confirmacoes.jsp";
    }
}
