package com.ecomponentes.formularios.perfil.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.ecomponentes.formularios.empresa.bo.EmpresaBO;
import com.ecomponentes.formularios.perfil.bo.PerfilBO;
import com.ecomponentes.formularios.perfil.form.PerfilEditForm;
import com.ecomponentes.formularios.perfil.to.PerfilTO;
import com.ecomponentes.formularios.perfilempresa.bo.PerfilEmpresaBO;
import com.ecomponentes.formularios.perfilempresa.to.PerfilEmpresaTO;

public class PerfilEditAction extends DispatchAction {

    public ActionForward editPerfil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("editPerfil");
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        PerfilBO perfilBO = new PerfilBO();
        PerfilEmpresaBO perfilEmpresaBO = new PerfilEmpresaBO();
        perfilForm.setTo(perfilBO.getPerfil(id));
        request.getSession().setAttribute("perfilempresato", perfilForm.getTo().getPerfilEmpresaTO());
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
    public ActionForward deletePerfil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("deletePerfil");
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        PerfilBO perfilBO = new PerfilBO();
        perfilBO.removePerfil(id);
        return mapping.findForward("showList");
    }

    /** 
     * deletes a book from the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward delAreaPerfil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("deletePerfil");
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        PerfilBO perfilBO = new PerfilBO();
        perfilBO.removePerfil(id);
        return mapping.findForward("closeWindow");
    }

    /** 
     * updates or creates the book in the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward savePerfil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        PerfilBO perfilBO = new PerfilBO();
        if (request.getSession().getAttribute("perfilempresato") != null) {
            perfilForm.getTo().setPerfilEmpresaTO((PerfilEmpresaTO[]) request.getSession().getAttribute("perfilempresato"));
            request.getSession().removeAttribute("perfilempresato");
        }
        perfilBO.inserirAlterar(perfilForm.getTo());
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
    public ActionForward addEmpresa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EmpresaBO empresaBO = new EmpresaBO();
        boolean isOld = false;
        PerfilEditForm perfilEditForm = (PerfilEditForm) form;
        PerfilTO perfilTO = perfilEditForm.getTo();
        if (request.getSession().getAttribute("perfilempresato") == null) {
            request.getSession().setAttribute("perfilempresato", perfilEditForm.getTo().getPerfilEmpresaTO());
        } else {
            perfilTO.setPerfilEmpresaTO((PerfilEmpresaTO[]) request.getSession().getAttribute("perfilempresato"));
        }
        int arrayTamanho = (perfilTO.getPerfilEmpresaTO().length);
        PerfilEmpresaTO[] perfilEmpresaTO = new PerfilEmpresaTO[arrayTamanho + 1];
        for (int i = 0; i < perfilTO.getPerfilEmpresaTO().length; i++) {
            perfilEmpresaTO[i] = perfilTO.getPerfilEmpresaTO()[i];
            if (perfilEmpresaTO[i].tbEmpresa.idEmpresa.equals(perfilEditForm.getIdEmpresa())) {
                isOld = true;
            }
        }
        if (!isOld) {
            perfilEmpresaTO[arrayTamanho] = new PerfilEmpresaTO();
            perfilEmpresaTO[arrayTamanho].setTbPerfil(perfilEditForm.getTo());
            perfilEmpresaTO[arrayTamanho].setTbEmpresa(empresaBO.getEmpresa(new Integer(perfilEditForm.getIdEmpresa())));
            perfilEmpresaTO[arrayTamanho].setPrioridade(perfilEditForm.getPerfilEmpresaTO().prioridade);
            perfilTO.setPerfilEmpresaTO(perfilEmpresaTO);
            perfilEditForm.setTo(perfilTO);
            request.getSession().setAttribute("perfilempresato", perfilEmpresaTO);
        }
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
    public ActionForward delEmpresa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PerfilEditForm perfilEditForm = (PerfilEditForm) form;
        EmpresaBO empresaBO = new EmpresaBO();
        PerfilTO perfilTO = perfilEditForm.getTo();
        PerfilEmpresaTO[] perfilEmpresaTO = (PerfilEmpresaTO[]) request.getSession().getAttribute("perfilempresato");
        PerfilEmpresaTO[] newPerfilEmpresaTO = new PerfilEmpresaTO[perfilEmpresaTO.length - 1];
        String id = perfilEditForm.getIdPerfilEmpresa();
        int count = 0;
        for (int i = 0; i < perfilEmpresaTO.length; i++) {
            if (!perfilEmpresaTO[i].getTbEmpresa().getIdEmpresa().equals(id)) {
                newPerfilEmpresaTO[count] = perfilEmpresaTO[i];
                count++;
            }
        }
        perfilTO.setPerfilEmpresaTO(newPerfilEmpresaTO);
        request.getSession().setAttribute("perfilempresato", newPerfilEmpresaTO);
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
    public ActionForward cancelar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        form = null;
        request.removeAttribute("perfilEditForm");
        request.getSession().removeAttribute("perfilempresato");
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
    public ActionForward savePerfilEmpresa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        PerfilBO perfilBO = new PerfilBO();
        PerfilEmpresaTO to = new PerfilEmpresaTO();
        to.getTbEmpresa().setIdEmpresa(perfilForm.getIdPerfilEmpresa());
        to.setPrioridade("1");
        perfilBO.inserirAlterar(perfilForm.getTo(), to);
        return mapping.findForward("closeWindow");
    }

    /** 
     * updates or creates the book in the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward carregaPerfilEmpresa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        perfilForm.setIdPerfilEmpresa(request.getParameter("id"));
        return mapping.findForward("showPopup");
    }

    /** 
     * updates or creates the book in the database
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward newPerfil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        form = null;
        request.removeAttribute("perfilEditForm");
        request.getSession().removeAttribute("perfilempresato");
        return mapping.findForward("showEdit");
    }

    public ActionForward editPopPerfil(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("editPerfil");
        PerfilEditForm perfilForm = (PerfilEditForm) form;
        Integer id = Integer.valueOf(request.getParameter("id"));
        PerfilBO perfilBO = new PerfilBO();
        perfilForm.setTo(perfilBO.getPerfil(id));
        return mapping.findForward("showPopup");
    }
}
