package it.uniromadue.portaleuni.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import it.uniromadue.portaleuni.base.BaseAction;
import it.uniromadue.portaleuni.dto.Utenti;
import it.uniromadue.portaleuni.form.DesignaPresidenteForm;
import it.uniromadue.portaleuni.manager.Manager;
import it.uniromadue.portaleuni.manager.SessionManager;
import it.uniromadue.portaleuni.service.ServiceLocator;
import it.uniromadue.portaleuni.utils.Conf;

public class DesignaPresidenteAction extends BaseAction {

    public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DesignaPresidenteForm specForm = (DesignaPresidenteForm) form;
        Utenti user = SessionManager.getUser(request.getSession(false));
        specForm.setUtente(user);
        Manager manager = ServiceLocator.getManager(springContext, "designaPresidenteManager");
        manager.loadData(specForm);
        request.setAttribute(Conf.UTENTI, specForm.getUtenti());
        return mapping.findForward("designaPresidente");
    }

    public ActionForward designa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DesignaPresidenteForm specForm = (DesignaPresidenteForm) form;
        Utenti user = SessionManager.getUser(request.getSession(false));
        specForm.setUtente(user);
        Manager manager = ServiceLocator.getManager(springContext, "designaPresidenteManager");
        manager.saveData(specForm);
        manager.loadData(specForm);
        request.setAttribute(Conf.UTENTI, specForm.getUtenti());
        return mapping.findForward("designaPresidente");
    }
}
