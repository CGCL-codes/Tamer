package it.uniromadue.portaleuni.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import it.uniromadue.portaleuni.base.BaseAction;
import it.uniromadue.portaleuni.dto.Utenti;
import it.uniromadue.portaleuni.form.OrarioLezioniForm;
import it.uniromadue.portaleuni.manager.Manager;
import it.uniromadue.portaleuni.manager.SessionManager;
import it.uniromadue.portaleuni.service.ServiceLocator;

public class OrarioLezioniAction extends BaseAction {

    public ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrarioLezioniForm orarioLezioniForm = (OrarioLezioniForm) form;
        Utenti utente = (Utenti) SessionManager.getUser(request.getSession(false));
        if (utente != null) {
            orarioLezioniForm.setLoggato(1);
            orarioLezioniForm.setUtenteLoggato(utente);
        } else {
            orarioLezioniForm.setLoggato(2);
        }
        Manager orarioLezioniManger = ServiceLocator.getManager(springContext, "orarioLezioniManager");
        orarioLezioniManger.loadData(orarioLezioniForm);
        request.setAttribute("lezioniPrimo", orarioLezioniForm.getLezioniPrimo());
        request.setAttribute("lezioniSecondo", orarioLezioniForm.getLezioniSecondo());
        request.setAttribute("lezioniTerzo", orarioLezioniForm.getLezioniTerzo());
        request.setAttribute("lezioniQuarto", orarioLezioniForm.getLezioniQuarto());
        request.setAttribute("lezioniQuinto", orarioLezioniForm.getLezioniQuinto());
        request.setAttribute("listaInsegnamenti", orarioLezioniForm.getListaInsegnamenti());
        request.setAttribute("listaAule", orarioLezioniForm.getListaAule());
        return mapping.findForward("lezioni");
    }

    public ActionForward saveLezione(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrarioLezioniForm orarioLezioniForm = (OrarioLezioniForm) form;
        Manager orarioLezioniManger = ServiceLocator.getManager(springContext, "orarioLezioniManager");
        orarioLezioniManger.saveData(orarioLezioniForm);
        return mapping.findForward("ricarica");
    }

    public ActionForward deleteLezione(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OrarioLezioniForm orarioLezioniForm = (OrarioLezioniForm) form;
        Manager orarioLezioniManger = ServiceLocator.getManager(springContext, "orarioLezioniManager");
        orarioLezioniManger.deleteData(orarioLezioniForm);
        return mapping.findForward("ricarica");
    }
}
