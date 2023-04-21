package br.ufc.quixada.adrs.comandos.admin;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Visita;
import br.ufc.quixada.adrs.service.FaixaEtariaService;
import br.ufc.quixada.adrs.service.TipoAnimalService;
import br.ufc.quixada.adrs.service.VisitaService;
import br.ufc.quixada.adrs.util.Msg;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Caio
 */
public class CmdAdminVisualizarVisita implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        VisitaService vs = new VisitaService();
        Visita visita = vs.getVisitaById(Long.parseLong(request.getParameter("id")));
        if (visita == null) {
            session.setAttribute("erro", Msg.VISITA_NOT_FOUND);
            return "/admin/admin_index.jsp";
        }
        session.setAttribute("tiposAnimais", new TipoAnimalService().getAllTiposAnimais());
        session.setAttribute("faixasEtarias", new FaixaEtariaService().getAllFaixasEtarias());
        session.setAttribute("visita", visita);
        return "/admin/admin_visualizar_visita.jsp";
    }
}
