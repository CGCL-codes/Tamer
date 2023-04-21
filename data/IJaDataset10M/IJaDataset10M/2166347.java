package br.ufc.quixada.adrs.comandos.admin;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Adrs;
import br.ufc.quixada.adrs.model.Prazo;
import br.ufc.quixada.adrs.service.AdrsService;
import br.ufc.quixada.adrs.service.PrazoService;
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
public class CmdAddAdrsPrazo implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession sessao = request.getSession(true);
        Long idPrazo = (Long) sessao.getAttribute("id_prazo");
        List<Adrs> listaFinaAdrs = new ArrayList<Adrs>();
        AdrsService as = new AdrsService();
        boolean cb3 = request.getParameter("cb2") != null ? true : false;
        boolean cb2 = request.getParameter("cb") != null ? true : false;
        if (cb3 || cb2) {
            if (cb3) {
                String cb[] = request.getParameterValues("cb2");
                for (int i = 0; i < cb.length; i++) {
                    listaFinaAdrs.add(as.getAdrsById(Long.parseLong(cb[i])));
                }
            }
            if (cb2) {
                String cb4[] = request.getParameterValues("cb");
                int entrou = 0;
                for (int i = 0; i < cb4.length; i++) {
                    if (listaFinaAdrs.isEmpty()) {
                        listaFinaAdrs.add(as.getAdrsById(Long.parseLong(cb4[i])));
                    } else {
                        for (Adrs adrsTemp : listaFinaAdrs) {
                            if (adrsTemp.getId().equals(Long.parseLong(cb4[i]))) {
                                entrou = 1;
                                break;
                            }
                        }
                        if (entrou == 0) {
                            listaFinaAdrs.add(as.getAdrsById(Long.parseLong(cb4[i])));
                        }
                    }
                }
            }
        } else {
            sessao.setAttribute("erro", Msg.EMPTY_SELECTION_ERROR_ADRS);
            return "/admin/admin_gerenciar_add_adrs_fora_prazo.jsp";
        }
        PrazoService ps = new PrazoService();
        Prazo p = ps.getPrazoById(idPrazo);
        List<Adrs> listaTemp = p.getListaAdrs();
        for (Adrs adrs : listaFinaAdrs) {
            listaTemp.add(adrs);
        }
        p.setListaAdrs(listaTemp);
        ps.updatePrazo(p);
        sessao.setAttribute("sucesso", Msg.SUCCESSFULLY_ADDED);
        return "/admin/admin_gerenciar_prazo.jsp";
    }
}
