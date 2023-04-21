package com.ecomponentes.formularios.classificacaoempresa.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.ecomponentes.formularios.classificacaoempresa.bo.ClassificacaoEmpresaBO;
import com.ecomponentes.formularios.classificacaoempresa.form.ClassificacaoEmpresaEditForm;
import com.ecomponentes.formularios.empresa.bo.EmpresaBO;
import com.ecomponentes.formularios.empresa.to.EmpresaTO;

public class ClassificacaoEmpresaEditAction extends DispatchAction {

    public ActionForward editClassificacaoEmpresa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("editClassificacao");
        ClassificacaoEmpresaEditForm classificacaoEmpresaEditForm = (ClassificacaoEmpresaEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        EmpresaBO empresaBO = new EmpresaBO();
        EmpresaTO empresaTO = empresaBO.getEmpresa(id);
        classificacaoEmpresaEditForm.getTo().tbEmpresa.setIdEmpresa(request.getParameter("id"));
        return mapping.findForward("showEdit");
    }

    /** 
     * updates or creates the book in the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward saveClassificacaoEmpresa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ClassificacaoEmpresaEditForm classificacaoEmpresaEditForm = (ClassificacaoEmpresaEditForm) form;
        ClassificacaoEmpresaBO classificacaoEmpresaBO = new ClassificacaoEmpresaBO();
        classificacaoEmpresaBO.gravaClassificacaoEmpresaTrabalho(new Integer(classificacaoEmpresaEditForm.getTo().tbEmpresa.idEmpresa), classificacaoEmpresaEditForm.getAssigned());
        return mapping.findForward("closeWindow");
    }
}
