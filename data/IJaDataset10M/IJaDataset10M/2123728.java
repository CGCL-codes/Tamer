package br.ufrj.cad.view.disciplina;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import br.ufrj.cad.model.bo.AnoBase;
import br.ufrj.cad.model.bo.Disciplina;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.disciplina.DisciplinaService;
import br.ufrj.cad.model.to.AnoBaseTO;
import br.ufrj.cad.model.to.DisciplinaTO;
import br.ufrj.cad.view.CADBaseAction;

public class CriarAnoBaseAction extends CADBaseAction {

    @Override
    protected void executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws Exception {
        DisciplinaForm disciplinaForm = (DisciplinaForm) form;
        String idAnoBaseReferencia = StringUtils.trimToNull(disciplinaForm.getIdAnoBase());
        String valorAnoBase = StringUtils.trimToNull(disciplinaForm.getValorAnoBase());
        AnoBase anoBaseCriado = DisciplinaService.getInstance().criaAnoBase(valorAnoBase, usuario.getDepartamento(), idAnoBaseReferencia);
        AnoBaseTO anoBaseTO = DisciplinaHelper.obtemTOAnoBase(anoBaseCriado);
        List<Disciplina> disciplinas = DisciplinaActionHelper.executaPesquisaDisciplinas(new DisciplinaTO(), anoBaseTO, usuario.getDepartamento());
        request.setAttribute(RESULTADO_BUSCA, DisciplinaHelper.obtemListaDisciplinaTO(disciplinas));
        addSucessMessage(request, "_nls.mensagem.disciplina.anobase.inicializado.sucesso");
    }
}
