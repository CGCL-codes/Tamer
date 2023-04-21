package br.ufrj.cad.view.banca;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import br.ufrj.cad.model.bancaprofessor.BancaProfessorService;
import br.ufrj.cad.model.bo.Banca;
import br.ufrj.cad.model.bo.BancaProfessor;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.to.BancaProfessorTO;
import br.ufrj.cad.view.CADBaseAction;

public class AlterarBancaProfessorAction extends CADBaseAction {

    @Override
    protected void executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws Exception {
        BancaProfessorForm bancaProfessorForm = (BancaProfessorForm) form;
        if (bancaProfessorForm.getBancaProfessorId() == null || bancaProfessorForm.getBancaProfessorId().length() == 0) return;
        BancaProfessorTO filtro = new BancaProfessorTO();
        filtro.setId(bancaProfessorForm.getBancaProfessorId());
        BancaProfessor bancaProfessorNoBanco = BancaProfessorService.getInstance().obtemBancaProfessorPorId(new BancaProfessor(filtro).getId());
        bancaProfessorForm.preencheFormComObjeto(bancaProfessorNoBanco);
    }
}
