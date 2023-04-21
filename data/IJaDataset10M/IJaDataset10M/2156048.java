package com.odontosis.view.servicoconveniado;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.odontosis.entidade.ServicoConveniado;
import com.odontosis.service.ServicoConveniadoService;
import com.odontosis.util.StringUtilsOdontosis;
import com.odontosis.view.OdontosisAction;

public class AcaoListaServicoConvenio extends OdontosisAction<ServicoConveniadoService> {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FormListaServicoConvenio form = (FormListaServicoConvenio) actionform;
        if (!StringUtilsOdontosis.isVazia(form.getMetodo()) && form.getMetodo().equals("deletar")) {
            onDeletar(mapping, actionform, request, response);
        }
        if (!StringUtilsOdontosis.isVazia(form.getMetodo()) && form.getMetodo().equals("impressao")) {
            return onImprimir(mapping, actionform, request, response);
        }
        String nomePaciente = form.getNomePaciente();
        String pasta = form.getNumeroPasta();
        Date inicio = StringUtilsOdontosis.StringToDate(form.getDataInicial());
        Date fim = StringUtilsOdontosis.StringToDate(form.getDataFinal());
        Collection<ServicoConveniado> servicosConveniados = service.pesquisarPorPacientePastaPeriodo(nomePaciente, pasta, inicio, fim);
        request.setAttribute("colecao", servicosConveniados);
        return mapping.getInputForward();
    }

    public ActionForward onImprimir(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FormListaServicoConvenio form = (FormListaServicoConvenio) actionform;
        Long id = Long.parseLong(form.getImpressao());
        ServicoConveniado servicoConveniado = service.load(id);
        return AcaoServicoConvenio.imprimirFiod(mapping, request, servicoConveniado, true);
    }

    public void onDeletar(ActionMapping mapping, ActionForm actionform, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FormListaServicoConvenio form = (FormListaServicoConvenio) actionform;
        long id = Long.parseLong(form.getExcluido());
        service.excluirLogicamente(service.load(id));
        Collection<String> sucesso = new ArrayList<String>();
        sucesso.add("Registro Deletado Com Sucesso");
        request.setAttribute("sucesso", sucesso);
        request.setAttribute("mensagens", null);
    }
}
