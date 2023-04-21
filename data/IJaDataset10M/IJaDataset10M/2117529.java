package ufpr.br.tsi.dacjaime.academico.secretaria.action;

import gov.pr.celepar.framework.action.BaseDispatchAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ufpr.br.tsi.dacjaime.academico.secretaria.form.ManterSolicitacaoForm;

public class ManterSolicitacaoAction extends BaseDispatchAction {

    public static final String GLOBAL_FORWARD_error = "error";

    public static final String GLOBAL_FORWARD_inicio = "inicio";

    public static final String FORWARD_editarAluno = "nonono";

    /**
     * Este metodo tem a funcao de encaminhar a chamada para o metodo default, caso ?action=nonono nao tenha sido expecificado
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return editar(mapping, form, request, response);
    }

    public ActionForward editar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        setActionForward(mapping.findForward(GLOBAL_FORWARD_error));
        ManterSolicitacaoForm frm = (ManterSolicitacaoForm) form;
        return mapping.findForward(FORWARD_editarAluno);
    }
}
