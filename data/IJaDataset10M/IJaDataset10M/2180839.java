package com.ecomponentes.formularios.moeda.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.ecomponentes.formularios.moeda.bo.MoedaBO;
import com.ecomponentes.formularios.moeda.form.MoedaEditForm;

public class MoedaEditAction extends DispatchAction {

    public ActionForward editMoeda(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("editMoeda");
        MoedaEditForm moedaForm = (MoedaEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        MoedaBO moedaBO = new MoedaBO();
        moedaForm.setTo(moedaBO.getMoeda(id));
        return mapping.findForward("showEdit");
    }

    /** 
     * deletes a book from the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward deleteMoeda(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("deleteMoeda");
        MoedaEditForm moedaForm = (MoedaEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        MoedaBO moedaBO = new MoedaBO();
        moedaBO.removeMoeda(id);
        return mapping.findForward("showList");
    }

    /** 
     * updates or creates the book in the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward saveMoeda(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MoedaEditForm moedaForm = (MoedaEditForm) form;
        MoedaBO moedaBO = new MoedaBO();
        moedaBO.inserirAlterar(moedaForm.getTo());
        return mapping.findForward("showList");
    }
}
