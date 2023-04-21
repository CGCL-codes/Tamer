package br.ufc.quixada.adrs.comandos.visit;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Doenca;
import br.ufc.quixada.adrs.model.Produtor;
import br.ufc.quixada.adrs.model.TipoDoenca;
import br.ufc.quixada.adrs.model.Visita;
import br.ufc.quixada.adrs.util.Msg;
import br.ufc.quixada.adrs.util.UtilAdrs;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Escritorio projetos
 */
public class CmdVisitMontaRelatorioQSProdutoresPorDoencas implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        List<TipoDoenca> tiposDoencas = (List<TipoDoenca>) session.getAttribute("doencas");
        List<Produtor> produtoresSelecionados = (ArrayList<Produtor>) session.getAttribute("produtoresSelecionadoQS");
        List<TipoDoenca> tiposDoencasSelecionados = new ArrayList<TipoDoenca>();
        String todosPeriodos = request.getParameter("todosPeriodos");
        String dataIni = request.getParameter("dataInicio");
        String dataFim = request.getParameter("dataFim");
        String selectOperacao = request.getParameter("operacao");
        String sValor = request.getParameter("valor");
        boolean tdPeriodosSelected = false;
        if (todosPeriodos != null) {
            tdPeriodosSelected = true;
        }
        if (!tdPeriodosSelected) {
            if (!UtilAdrs.validaData(dataIni)) {
                session.setAttribute("erro", Msg.INPUT_ERROR_WRONG_START_DATE_FORMAT);
                return "/visit/visit_relatorio_passo_4_filtros_doencas.jsp";
            } else if (!UtilAdrs.validaData(dataFim)) {
                session.setAttribute("erro", Msg.INPUT_ERROR_WRONG_END_DATE_FORMAT);
                return "/visit/visit_relatorio_passo_4_filtros_doencas.jsp";
            }
        }
        if (!UtilAdrs.isNumber(sValor)) {
            session.setAttribute("erro", Msg.NOT_POSITIVE_NUMERIC);
            return "/visit/visit_relatorio_passo_4_filtros_doencas.jsp";
        }
        boolean hasDoencaComboBoxSelected = request.getParameter("doenca") != null ? true : false;
        if (hasDoencaComboBoxSelected) {
            String arrayDoencasIDComboBox[] = request.getParameterValues("doenca");
            for (int i = 0; i < arrayDoencasIDComboBox.length; i++) {
                TipoDoenca tDoenca = getTipoDoencaById(tiposDoencas, Long.parseLong(arrayDoencasIDComboBox[i]));
                tiposDoencasSelecionados.add(tDoenca);
            }
        } else {
            session.setAttribute("erro", Msg.EMPTY_SELECTION_ERROR_TIPO_DOENCA);
            return "/visit/visit_relatorio_passo_4_filtros_doencas.jsp";
        }
        Date ini = UtilAdrs.treatToDate(dataIni);
        Date fim = UtilAdrs.treatToDate(dataFim);
        List<Produtor> produtoresResult = new ArrayList<Produtor>();
        for (Produtor produtor : produtoresSelecionados) {
            Integer[] qtdeDoencas = new Integer[tiposDoencasSelecionados.size()];
            for (int k = 0; k < qtdeDoencas.length; k++) {
                qtdeDoencas[k] = new Integer(0);
            }
            List<Visita> visitas = produtor.getVisitas();
            for (Visita visita : visitas) {
                if (!visita.hasQuestionarioSanitario()) {
                    continue;
                }
                if (tdPeriodosSelected || (!tdPeriodosSelected && visita.visitaBetweenInitialDateAndFinalDate(ini, fim))) {
                    for (int j = 0; j < tiposDoencasSelecionados.size(); j++) {
                        TipoDoenca tDoenca = tiposDoencasSelecionados.get(j);
                        Doenca doenca = visita.getQuestionarioSanitario().buscaDoenca(tDoenca.getId());
                        if (doenca != null) {
                            qtdeDoencas[j] += doenca.getQuantidadeDeCasos();
                        }
                    }
                }
            }
            if (isCompatibleWithCondtions(qtdeDoencas, selectOperacao, Integer.parseInt(sValor))) {
                produtoresResult.add(produtor);
            }
        }
        String periodo = "de " + UtilAdrs.getPrimeiraDataQuestionario(produtoresSelecionados) + " a " + UtilAdrs.getUltimaDataQuestionario(produtoresSelecionados);
        if (!tdPeriodosSelected) {
            periodo = "de " + dataIni + " a " + dataFim;
        }
        String sDoencasSelecionadas = "";
        for (TipoDoenca td : tiposDoencasSelecionados) {
            sDoencasSelecionadas += td.getNome() + ", ";
        }
        sDoencasSelecionadas = sDoencasSelecionadas.substring(0, sDoencasSelecionadas.length() - 2);
        String sConsulta = "";
        if (selectOperacao.equalsIgnoreCase("IGUAL")) {
            sConsulta = "Produtores que relataram exatamente " + sValor + " ocorrências das doenças selecionadas";
        } else if (selectOperacao.equalsIgnoreCase("MENOR")) {
            sConsulta = "Produtores que relataram menos que " + sValor + " ocorrências das doenças selecionadas";
        } else if (selectOperacao.equalsIgnoreCase("MAIOR")) {
            sConsulta = "Produtores que relataram mais que " + sValor + " ocorrências das doenças selecionadas";
        }
        session.setAttribute("sDoencasSelecionadas", sDoencasSelecionadas.toUpperCase());
        session.setAttribute("sConsulta", sConsulta.toUpperCase());
        session.setAttribute("periodo", periodo);
        session.setAttribute("listaProdutores", produtoresResult);
        return "/visit/relatorio_qs_gerado_prods_doencas.jsp";
    }

    private TipoDoenca getTipoDoencaById(List<TipoDoenca> tiposDoencas, long parseLong) {
        for (TipoDoenca tipoDoenca : tiposDoencas) {
            if (tipoDoenca.getId().equals(parseLong)) {
                return tipoDoenca;
            }
        }
        return null;
    }

    private boolean isCompatibleWithCondtions(Integer[] qtdeDoencas, String selectOperacao, Integer valor) {
        if (selectOperacao.equals("MAIOR")) {
            for (Integer inValor : qtdeDoencas) {
                if (inValor.intValue() <= valor.intValue()) {
                    return false;
                }
            }
            return true;
        } else if (selectOperacao.equals("MENOR")) {
            for (Integer inValor : qtdeDoencas) {
                if (inValor.intValue() >= valor.intValue()) {
                    return false;
                }
            }
            return true;
        } else if (selectOperacao.equals("IGUAL")) {
            for (Integer inValor : qtdeDoencas) {
                if (inValor.intValue() != valor.intValue()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
