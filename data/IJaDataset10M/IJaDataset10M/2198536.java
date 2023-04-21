package br.ufc.quixada.adrs.comandos.admin;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Prazo;
import br.ufc.quixada.adrs.service.AdrsService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Escritorio projetos
 */
public class CmdEditarPrazo implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Long idPrazo = Long.parseLong(request.getParameter("prazo_id"));
        List<Prazo> prazosAbertos = (List<Prazo>) request.getSession().getAttribute("prazosAbertos");
        AdrsService as = new AdrsService();
        session.setAttribute("listAdrsPrazo", as.getAllAdrs());
        for (Prazo prazo : prazosAbertos) {
            if (prazo.getId().compareTo(idPrazo) == 0) {
                session.setAttribute("prazo", prazo);
                break;
            }
        }
        return "/admin/admin_add_prazo.jsp";
    }
}
