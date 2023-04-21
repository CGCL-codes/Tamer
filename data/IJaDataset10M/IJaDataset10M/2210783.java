package org.fhw.cabaweb.webfrontend.actions.edit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fhw.cabaweb.data.DataInterfaceGruppenmitglieder;
import org.fhw.cabaweb.data.DataInterfaceProjekte;
import org.fhw.cabaweb.data.DataInterfaceErgebnissdatenGruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Gruppenmitglieder;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Gruppierungsnamen;
import org.fhw.cabaweb.webfrontend.configs.Keys;
import org.fhw.cabaweb.webfrontend.forms.simple.ErgebnissdatenGruppierungsnameForm;

/**
 * <strong>Action</strong>-Klasse f&uuml;r die Edit ErgebnissdatenGruppierungsname Action .
 * Die Controller Klasse der Struts Model View Controller Architektur.
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 16.07.2004
 */
public final class EditErgebnissdatenGruppierungsnameAction extends Action {

    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log = LogFactory.getLog("org.fhw.cabaweb.webfrontend.actions.edit");

    /**
     * Verarbeiten der spezifizierten HTTP Anfrage und erzeugen der zugeordneten
     * HTTP Antwort bzw. Forwarden an eine andere Web Komponente, die die Antwort 
     * erzeugt.  
     * 
     * Gibt eine <code>ActionForward</code> Instanz zur�ck die angibt wohin und wie
     * die Kontrolle weitergegeben werden soll. Kann auch <code>null</code> sein, 
     * wenn die Anfrage bereits bearbeitet wurde.
     * 
     * @param mapping Das ActionMapping das benutzt wurde um diese Instanz zu selektieren
     * @param form Das optionale ActionForm Bean f�r die Anfrage (soweit vorhanden)
     * @param request Die HTTP Anfrage die wir gerade bearbeiten
     * @param response The HTTP Antwort die wir erzeugen
     *
     * @return Die Action zu der wir weiterleiten
     * @exception Exception wenn ein Eingabe-/Ausgabe Fehler auftritt oder eine Servlet Exception auftritt
     *            bzw. die Business Logik einen Fehler verursacht 
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DataInterfaceErgebnissdatenGruppierungsnamen divgn = new DataInterfaceErgebnissdatenGruppierungsnamen();
        DataInterfaceGruppenmitglieder digm = new DataInterfaceGruppenmitglieder();
        DataInterfaceProjekte dip = new DataInterfaceProjekte();
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        boolean isAdmin = request.isUserInRole("Administrator");
        boolean isProjectleader = false;
        boolean isUser = false;
        String username = request.getRemoteUser();
        if (!isAdmin) {
            isProjectleader = request.isUserInRole("Projektleiter");
        }
        if (!isAdmin && !isProjectleader) {
            isUser = request.isUserInRole("Benutzer");
        }
        if (action == null) {
            action = "Create";
        }
        Integer gruppierungsnummer = null;
        String grpnummer = request.getParameter("gruppierungsnummer");
        if (grpnummer != null) {
            gruppierungsnummer = new Integer(grpnummer);
        }
        if (log.isDebugEnabled()) {
            log.debug("EditErgebnissdatenGruppierungsnameAction: Processing " + action + " action for Preset Groupname [" + gruppierungsnummer + "]");
        }
        Ergebnissdaten_Gruppierungsnamen ergebnissdatenGruppierungsname = (Ergebnissdaten_Gruppierungsnamen) divgn.sucheGruppierungsnummer(gruppierungsnummer);
        if ((ergebnissdatenGruppierungsname == null) && !action.equals("Create")) {
            if (log.isDebugEnabled()) {
                log.debug(" No Preset Groupname for gruppierungsnummer " + gruppierungsnummer);
            }
            return (mapping.findForward("failure"));
        }
        if (ergebnissdatenGruppierungsname != null) {
            session.setAttribute(Keys.VOREINSTELLUNGENGRUPPIERUNGSNAME_KEY, ergebnissdatenGruppierungsname);
        }
        Collection projekte = null;
        if (isAdmin) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: Administrator");
                log.debug(" Searching all Projects");
            }
            projekte = dip.sucheAlle();
            if (log.isDebugEnabled()) {
                log.debug(" Found " + projekte.size() + " Projects");
            }
        } else if (isProjectleader) {
            if (log.isDebugEnabled()) {
                log.debug(" User is NOT Administrator");
                log.debug(" Searching own Project");
            }
            Collection usernames = digm.sucheLogInName(username);
            Iterator iter = usernames.iterator();
            Gruppenmitglieder user = null;
            if (iter.hasNext()) {
                user = (Gruppenmitglieder) iter.next();
            }
            if (log.isDebugEnabled()) {
                log.debug(" Projektnummer " + ((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            }
            ArrayList projekteAL = new ArrayList();
            projekteAL.add(dip.sucheProjektnummer(((user.getProjektgruppe()).getProjekte()).getProjektnummer()));
            projekte = projekteAL;
            if (log.isDebugEnabled()) {
                log.debug(" Found " + projekte.size() + " Projects");
            }
        }
        if (projekte != null) {
            session.setAttribute(Keys.PROJEKTE_KEY, projekte);
        }
        if (form == null) {
            if (log.isDebugEnabled()) {
                log.debug(" Creating new ErgebnissdatenGruppierungsnameForm bean under key " + mapping.getAttribute());
            }
            form = new ErgebnissdatenGruppierungsnameForm();
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                session.setAttribute(mapping.getAttribute(), form);
            }
        }
        ErgebnissdatenGruppierungsnameForm prvgnform = (ErgebnissdatenGruppierungsnameForm) form;
        prvgnform.setAction(action);
        if (!action.equals("Create")) {
            if (log.isDebugEnabled()) {
                log.debug(" Populating form from " + ergebnissdatenGruppierungsname);
            }
            try {
                PropertyUtils.copyProperties(prvgnform, ergebnissdatenGruppierungsname);
                prvgnform.setGruppierungsnummer(gruppierungsnummer);
                prvgnform.setProjektnummer(ergebnissdatenGruppierungsname.getProjekte().getProjektnummer());
                prvgnform.setAction(action);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t == null) t = e;
                log.error("ErgebnissdatenGruppierungsnameForm.populate", t);
                throw new ServletException("ErgebnissdatenGruppierungsnameForm.populate", t);
            } catch (Throwable t) {
                log.error("ErgebnissdatenGruppierungsnameForm.populate", t);
                throw new ServletException("ErgebnissdatenGruppierungsnameForm.populate", t);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(" Forwarding to 'success' page");
        }
        return (mapping.findForward("success"));
    }
}
