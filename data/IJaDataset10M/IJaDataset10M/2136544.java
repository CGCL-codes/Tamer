package br.ufc.quixada.adrs.comandos.admin;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Adrs;
import br.ufc.quixada.adrs.model.Prazo;
import br.ufc.quixada.adrs.service.AdrsService;
import br.ufc.quixada.adrs.service.PrazoService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Welligton
 */
public class CmdListarAdrsForaPrazo implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        String sPrazoID = request.getParameter("prazo_id");
        Long idPrazo = Long.parseLong(sPrazoID);
        PrazoService ps = new PrazoService();
        Prazo p = ps.getPrazoById(idPrazo);
        List<Adrs> adrsListaFinal = new AdrsService().getAdrsNaoIncluidosNoPrazo(p);
        request.getSession().setAttribute("adrsForaPrazo", adrsListaFinal);
        request.getSession().setAttribute("id_prazo", idPrazo);
        return "/admin/admin_gerenciar_add_adrs_fora_prazo.jsp";
    }
}
