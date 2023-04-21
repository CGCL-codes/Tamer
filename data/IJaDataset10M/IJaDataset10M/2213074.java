package br.ufrj.cad.view.periodoavaliacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import br.ufrj.cad.model.bo.PeriodoAvaliacao;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.periodoavaliacao.PeriodoAvaliacaoService;
import br.ufrj.cad.view.CADBaseAction;

public class RemoverPeriodoAvaliacaoAction extends CADBaseAction {

    @Override
    protected void executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws Exception {
        String id = ((PeriodoAvaliacaoForm) form).getIdPeriodoAvaliacao();
        PeriodoAvaliacao periodoAvaliacao = new PeriodoAvaliacao();
        periodoAvaliacao.setId(new Long(id));
        periodoAvaliacao.delete();
        addSucessMessage(request, "_nls.mensagem.periodoinscriao.excluido.com.sucesso");
        salvaResultado(request, PeriodoAvaliacaoService.getInstance().obtemTodosPeriodosCadastrados(usuario));
    }
}
