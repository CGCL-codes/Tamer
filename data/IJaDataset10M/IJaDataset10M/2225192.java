package com.ecomponentes.formularios.anotacao.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.ecomponentes.formularios.empresa.bo.EmpresaBO;
import com.ecomponentes.formularios.empresaanotacao.form.EmpresaAnotacaoEditForm;

public class AddAnotacaoSelectAction extends DispatchAction {

    /**
	 * M�todo que lista os Buyers
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
    public ActionForward constroi(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String idEmpresa = request.getParameter("idEmpresa");
        EmpresaAnotacaoEditForm empresaAnotacaoEditForm = (EmpresaAnotacaoEditForm) form;
        EmpresaBO empresaBO = new EmpresaBO();
        empresaAnotacaoEditForm.getTo().setTbEmpresa(empresaBO.getEmpresa(new Integer(idEmpresa)));
        return mapping.findForward("empresa");
    }
}
