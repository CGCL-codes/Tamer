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
import org.fhw.cabaweb.data.DataInterfaceSprachen;
import org.fhw.cabaweb.data.DataInterfaceVoreinstellungenGruppierungsnamen;
import org.fhw.cabaweb.data.DataInterfaceVoreinstellungenFeldbeschreibungen;
import org.fhw.cabaweb.data.DataInterfaceVoreinstellungenFeldnamen;
import org.fhw.cabaweb.data.DataInterfaceVoreinstellungenUntergruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Gruppenmitglieder;
import org.fhw.cabaweb.ojb.dataobjects.Voreinstellungen_Feldbeschreibungen;
import org.fhw.cabaweb.webfrontend.configs.Keys;
import org.fhw.cabaweb.webfrontend.forms.simple.VoreinstellungenFeldbeschreibungForm;

/**
 * <strong>Action</strong>-Klasse f&uuml;r die Edit VoreinstellungenFeldbeschreibung Action .
 * Die Controller Klasse der Struts Model View Controller Architektur.
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 16.07.2004
 */
public final class EditVoreinstellungenFeldbeschreibungAction extends Action {

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
        DataInterfaceVoreinstellungenFeldbeschreibungen divfb = new DataInterfaceVoreinstellungenFeldbeschreibungen();
        DataInterfaceVoreinstellungenFeldnamen divfn = new DataInterfaceVoreinstellungenFeldnamen();
        DataInterfaceVoreinstellungenUntergruppierungsnamen divugn = new DataInterfaceVoreinstellungenUntergruppierungsnamen();
        DataInterfaceVoreinstellungenGruppierungsnamen divgn = new DataInterfaceVoreinstellungenGruppierungsnamen();
        DataInterfaceGruppenmitglieder digm = new DataInterfaceGruppenmitglieder();
        DataInterfaceProjekte dip = new DataInterfaceProjekte();
        DataInterfaceSprachen dis = new DataInterfaceSprachen();
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
        Integer feldnummer = null;
        String fnummer = request.getParameter("feldnummer");
        Integer untergruppierungsnummer = null;
        String ugrpnummer = request.getParameter("untergruppierungsnummer");
        Integer gruppierungsnummer = null;
        String grpnummer = request.getParameter("gruppierungsnummer");
        Integer projektnummer = null;
        String prjnummer = request.getParameter("projektnummer");
        Integer sprachnummer = null;
        String sprnummer = request.getParameter("sprachnummer");
        if (fnummer != null) {
            feldnummer = new Integer(fnummer);
        }
        if (ugrpnummer != null) {
            untergruppierungsnummer = new Integer(ugrpnummer);
        }
        if (grpnummer != null) {
            gruppierungsnummer = new Integer(grpnummer);
        }
        if (prjnummer != null) {
            projektnummer = new Integer(prjnummer);
        }
        if (sprnummer != null) {
            sprachnummer = new Integer(sprnummer);
        }
        if (log.isDebugEnabled()) {
            log.debug("EditVoreinstellungenFeldbeschreibungAction: Processing " + action + " action for Preset Fielddescription [" + feldnummer + "]");
        }
        Collection voreinstellungenFeldbeschreibungen = divfb.sucheKombination(projektnummer, feldnummer, gruppierungsnummer, untergruppierungsnummer, sprachnummer);
        Iterator iter = voreinstellungenFeldbeschreibungen.iterator();
        Voreinstellungen_Feldbeschreibungen voreinstellungenFeldbeschreibung = null;
        if (iter.hasNext()) {
            voreinstellungenFeldbeschreibung = (Voreinstellungen_Feldbeschreibungen) iter.next();
        }
        if ((voreinstellungenFeldbeschreibung == null) && !action.equals("Create")) {
            if (log.isDebugEnabled()) {
                log.debug(" No Preset Fielddescription for feldnummer " + feldnummer);
            }
            return (mapping.findForward("failure"));
        }
        if (voreinstellungenFeldbeschreibung != null) {
            session.setAttribute(Keys.VOREINSTELLUNGENUNTERGRUPPIERUNGSBESCHREIBUNG_KEY, voreinstellungenFeldbeschreibung);
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
            iter = usernames.iterator();
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
        Collection voreinstellungenFeldnamen = null;
        if (isAdmin) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: Administrator");
                log.debug(" Searching all Fieldnames");
            }
            voreinstellungenFeldnamen = divfn.sucheAlle();
            if (log.isDebugEnabled()) {
                log.debug(" Found " + voreinstellungenFeldnamen.size() + " Fieldnames");
            }
        } else if (isProjectleader) {
            if (log.isDebugEnabled()) {
                log.debug(" User is NOT Administrator");
                log.debug(" Searching own Fieldnames");
            }
            Collection usernames = digm.sucheLogInName(username);
            iter = usernames.iterator();
            Gruppenmitglieder user = null;
            if (iter.hasNext()) {
                user = (Gruppenmitglieder) iter.next();
            }
            if (log.isDebugEnabled()) {
                log.debug(" Projektnummer " + ((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            }
            voreinstellungenFeldnamen = divfn.sucheProjektnummer(((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            if (log.isDebugEnabled()) {
                log.debug(" Found " + voreinstellungenFeldnamen.size() + " Fieldnames");
            }
        }
        if (voreinstellungenFeldnamen != null) {
            session.setAttribute(Keys.VOREINSTELLUNGENFELDNAMEN_KEY, voreinstellungenFeldnamen);
        }
        Collection voreinstellungenUntergruppierungsnamen = null;
        if (isAdmin) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: Administrator");
                log.debug(" Searching all Sub-Groupnames");
            }
            voreinstellungenUntergruppierungsnamen = divugn.sucheAlle();
            if (log.isDebugEnabled()) {
                log.debug(" Found " + voreinstellungenUntergruppierungsnamen.size() + " Sub-Groupnames");
            }
        } else if (isProjectleader) {
            if (log.isDebugEnabled()) {
                log.debug(" User is NOT Administrator");
                log.debug(" Searching own Sub-Groupnames");
            }
            Collection usernames = digm.sucheLogInName(username);
            iter = usernames.iterator();
            Gruppenmitglieder user = null;
            if (iter.hasNext()) {
                user = (Gruppenmitglieder) iter.next();
            }
            if (log.isDebugEnabled()) {
                log.debug(" Projektnummer " + ((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            }
            voreinstellungenUntergruppierungsnamen = divugn.sucheProjektnummer(((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            if (log.isDebugEnabled()) {
                log.debug(" Found " + voreinstellungenUntergruppierungsnamen.size() + " Sub-Groupnames");
            }
        }
        if (voreinstellungenUntergruppierungsnamen != null) {
            session.setAttribute(Keys.VOREINSTELLUNGENUNTERGRUPPIERUNGSNAMEN_KEY, voreinstellungenUntergruppierungsnamen);
        }
        Collection voreinstellungenGruppierungsnamen = null;
        if (isAdmin) {
            if (log.isDebugEnabled()) {
                log.debug(" User has Role: Administrator");
                log.debug(" Searching all Groupnames");
            }
            voreinstellungenGruppierungsnamen = divgn.sucheAlle();
            if (log.isDebugEnabled()) {
                log.debug(" Found " + voreinstellungenGruppierungsnamen.size() + " Groupnames");
            }
        } else if (isProjectleader) {
            if (log.isDebugEnabled()) {
                log.debug(" User is NOT Administrator");
                log.debug(" Searching own Groupnames");
            }
            Collection usernames = digm.sucheLogInName(username);
            iter = usernames.iterator();
            Gruppenmitglieder user = null;
            if (iter.hasNext()) {
                user = (Gruppenmitglieder) iter.next();
            }
            if (log.isDebugEnabled()) {
                log.debug(" Projektnummer " + ((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            }
            voreinstellungenGruppierungsnamen = divgn.sucheProjektnummer(((user.getProjektgruppe()).getProjekte()).getProjektnummer());
            if (log.isDebugEnabled()) {
                log.debug(" Found " + voreinstellungenGruppierungsnamen.size() + " Groupnames");
            }
        }
        if (voreinstellungenGruppierungsnamen != null) {
            session.setAttribute(Keys.VOREINSTELLUNGENGRUPPIERUNGSNAMEN_KEY, voreinstellungenGruppierungsnamen);
        }
        Collection sprachen = dis.sucheAlle();
        if (sprachen != null) {
            session.setAttribute(Keys.SPRACHEN_KEY, sprachen);
        }
        if (form == null) {
            if (log.isDebugEnabled()) {
                log.debug(" Creating new VoreinstellungenFeldbeschreibungForm bean under key " + mapping.getAttribute());
            }
            form = new VoreinstellungenFeldbeschreibungForm();
            if ("request".equals(mapping.getScope())) {
                request.setAttribute(mapping.getAttribute(), form);
            } else {
                session.setAttribute(mapping.getAttribute(), form);
            }
        }
        VoreinstellungenFeldbeschreibungForm prvugbform = (VoreinstellungenFeldbeschreibungForm) form;
        prvugbform.setAction(action);
        if (!action.equals("Create")) {
            if (log.isDebugEnabled()) {
                log.debug(" Populating form from " + voreinstellungenFeldbeschreibung);
            }
            try {
                PropertyUtils.copyProperties(prvugbform, voreinstellungenFeldbeschreibung);
                prvugbform.setFeldnummer(voreinstellungenFeldbeschreibung.getVoreinstellungen_feldnamen().getFeldnummer());
                prvugbform.setUntergruppierungsnummer(voreinstellungenFeldbeschreibung.getVoreinstellungen_feldnamen().getVoreinstellungen_untergruppierungsnamen().getUntergruppierungsnummer());
                prvugbform.setGruppierungsnummer(voreinstellungenFeldbeschreibung.getVoreinstellungen_feldnamen().getVoreinstellungen_untergruppierungsnamen().getVoreinstellungen_gruppierungsnamen().getGruppierungsnummer());
                prvugbform.setProjektnummer(voreinstellungenFeldbeschreibung.getVoreinstellungen_feldnamen().getVoreinstellungen_untergruppierungsnamen().getVoreinstellungen_gruppierungsnamen().getProjekte().getProjektnummer());
                prvugbform.setSprachnummer(voreinstellungenFeldbeschreibung.getSprachen().getSprachnummer());
                prvugbform.setAction(action);
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();
                if (t == null) t = e;
                log.error("VoreinstellungenFeldbeschreibungForm.populate", t);
                throw new ServletException("VoreinstellungenFeldbeschreibungForm.populate", t);
            } catch (Throwable t) {
                log.error("VoreinstellungenFeldbeschreibungForm.populate", t);
                throw new ServletException("VoreinstellungenFeldbeschreibungForm.populate", t);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(" Forwarding to 'success' page");
        }
        return (mapping.findForward("success"));
    }
}
