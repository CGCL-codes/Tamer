package br.ufrj.cad.view.professor.planilha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.usuario.UsuarioService;
import br.ufrj.cad.view.CADBaseAction;

public class EscolherUsuarioParaAuditoriaAction extends CADBaseAction {

    @Override
    protected void executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws Exception {
        Usuario.planilhaCorrenteComItens = null;
        request.setAttribute(RESULTADO_BUSCA, UsuarioService.getInstance().obtemUsuariosPorDepartamento(usuario.getDepartamento()));
    }
}
