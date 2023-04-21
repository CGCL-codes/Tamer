package org.fhw.cabaweb.webfrontend.actions.list;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fhw.cabaweb.data.DataInterfaceGruppenmitglieder;
import org.fhw.cabaweb.data.DataInterfaceErgebnissdatenUntergruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Gruppenmitglieder;
import org.fhw.cabaweb.webfrontend.configs.Keys;
import org.fhw.cabaweb.webfrontend.forms.multiple.ErgebnissdatenUntergruppierungsnamenForm;
import org.fhw.cabaweb.webfrontend.tools.Common;

/**
 * <strong>Action</strong>-Klasse f&uuml;r die List ErgebnissdatenUntergruppierungsnamen Action .
 * Die Controller Klasse der Struts Model View Controller Architektur.
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 14.07.2004
 */
public final class ListErgebnissdatenUntergruppierungsnamenAction extends Action {

    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log = LogFactory.getLog("org.fhw.cabaweb.webfrontend.actions.list");

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
        DataInterfaceErgebnissdatenUntergruppierungsnamen divugn = (DataInterfaceErgebnissdatenUntergruppierungsnamen) new DataInterfaceErgebnissdatenUntergruppierungsnamen();
        divugn.clearCache();
        DataInterfaceGruppenmitglieder dig = new DataInterfaceGruppenmitglieder();
        dig.clearCache();
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
            action = "List";
        }
        if (log.isDebugEnabled()) {
            log.debug("ListErgebnissdatenUntergruppierungsnamenAction: Processing " + action + " action");
        }
        Collection ergebnissdatenUntergruppierungsnamen = null;
        if (isAdmin) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: Administrator");
                log.debug(" Searching all Preset-Groups");
            }
            ergebnissdatenUntergruppierungsnamen = divugn.sucheAlle();
            if (log.isDebugEnabled()) {
                log.debug(" Found " + ergebnissdatenUntergruppierungsnamen.size() + " Preset-Groups");
            }
        } else if (isProjectleader) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: Projectleader");
                log.debug(" Searching Preset-Groups for Projectleader");
            }
            Collection usernames = dig.sucheLogInName(username);
            Iterator iter = usernames.iterator();
            Gruppenmitglieder user = null;
            while (iter.hasNext()) {
                user = (Gruppenmitglieder) iter.next();
            }
            if (log.isDebugEnabled()) {
                log.debug(" Projektnummer " + ((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            }
            ergebnissdatenUntergruppierungsnamen = divugn.sucheProjektnummer(((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            if (log.isDebugEnabled()) {
                log.debug(" Found " + ergebnissdatenUntergruppierungsnamen.size() + " Preset-Groups");
            }
        } else if (isUser) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: User");
                log.debug(" Do not Search");
            }
        }
        if (ergebnissdatenUntergruppierungsnamen == null) {
            if (log.isDebugEnabled()) {
                log.debug(" No Preset-Groups registered");
            }
            return (mapping.findForward("success"));
        }
        if (ergebnissdatenUntergruppierungsnamen != null) {
            session.setAttribute(Keys.ERGEBNISSDATENUNTERGRUPPIERUNGSNAMEN_KEY, ergebnissdatenUntergruppierungsnamen);
        }
        if (form == null) {
            if (log.isDebugEnabled()) {
                log.debug(" Creating new ErgebnissdatenUntergruppierungsnamenForm bean under key " + mapping.getAttribute());
            }
            form = new ErgebnissdatenUntergruppierungsnamenForm();
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                session.setAttribute(mapping.getAttribute(), form);
            }
        }
        ErgebnissdatenUntergruppierungsnamenForm pgpform = (ErgebnissdatenUntergruppierungsnamenForm) form;
        if (action.equals("List")) {
            if (log.isDebugEnabled()) {
                log.debug(" Populating form from " + ergebnissdatenUntergruppierungsnamen);
            }
            try {
                pgpform.setErgebnissdatenUntergruppierungsnamen(ergebnissdatenUntergruppierungsnamen);
            } catch (Throwable t) {
                log.error("ErgebnissdatenUntergruppierungsnamenForm.populate", t);
                throw new ServletException("ErgebnissdatenUntergruppierungsnamenForm.populate", t);
            }
        }
        Common.Pager(request, ergebnissdatenUntergruppierungsnamen.size());
        if (log.isDebugEnabled()) {
            log.debug(" Forwarding to 'success' page");
        }
        return (mapping.findForward("success"));
    }
}
