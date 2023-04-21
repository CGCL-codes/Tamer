package br.ufc.quixada.adrs.comandos.admin;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Prazo;
import br.ufc.quixada.adrs.service.PrazoService;
import br.ufc.quixada.adrs.util.UtilAdrs;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Collections;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Escritorio projetos
 */
public class CmdListarPrazosAbertos implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        PrazoService ps = new PrazoService();
        Date dataAtual = new Date();
        SimpleDateFormat pegaAno = new SimpleDateFormat("yyyy");
        String anoAtual = pegaAno.format(dataAtual);
        Date date = UtilAdrs.treatToDate("01/01/" + anoAtual);
        Long numeroPrazosNoAnoAtual = ps.countPrazosSuperioresAD(date);
        if (numeroPrazosNoAnoAtual == 0) {
            request.getSession().setAttribute("gerarPrazos", numeroPrazosNoAnoAtual);
        }
        request.getSession().removeAttribute("prazo");
        List<Prazo> prazosExibidosNoMes = ps.getAllPrazos();
        SimpleDateFormat pegaMesEAno = new SimpleDateFormat("MM/yyyy");
        for (int i = 0; i < prazosExibidosNoMes.size(); i++) {
            Prazo p = prazosExibidosNoMes.get(i);
            if (!pegaMesEAno.format(dataAtual).equals(pegaMesEAno.format(p.getFim())) && !pegaMesEAno.format(dataAtual).equals(pegaMesEAno.format(p.getInicio())) && (dataAtual.after(p.getFim()) || dataAtual.before(p.getInicio()))) {
                prazosExibidosNoMes.remove(p);
                i--;
            }
        }
        Collections.sort(prazosExibidosNoMes, PrazoService.comparadorPorFim);
        request.getSession().setAttribute("prazosAbertos", prazosExibidosNoMes);
        request.getSession().setAttribute("mesDeReferencia", UtilAdrs.treatToMonthYearString(dataAtual));
        request.getSession().setAttribute("dataMesReferencia", dataAtual);
        return "/admin/admin_gerenciar_prazo.jsp";
    }
}
