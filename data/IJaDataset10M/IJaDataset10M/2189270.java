package org.fhw.cabaweb.webfrontend.actions.save;

import java.lang.reflect.InvocationTargetException;
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
import org.apache.struts.util.MessageResources;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fhw.cabaweb.data.DataInterfaceGruppenmitglieder;
import org.fhw.cabaweb.data.DataInterfaceProjektbeschreibungen;
import org.fhw.cabaweb.data.DataInterfaceProjekte;
import org.fhw.cabaweb.data.DataInterfaceSprachen;
import org.fhw.cabaweb.ojb.dataobjects.Gruppenmitglieder;
import org.fhw.cabaweb.ojb.dataobjects.Projektbeschreibungen;
import org.fhw.cabaweb.ojb.dataobjects.Projekte;
import org.fhw.cabaweb.ojb.dataobjects.Sprachen;
import org.fhw.cabaweb.tools.StringUtilities;
import org.fhw.cabaweb.webfrontend.configs.Keys;
import org.fhw.cabaweb.webfrontend.forms.simple.ProjektbeschreibungForm;

/**
 * <strong>Action</strong>-Klasse f&uuml;r die Save Projektbeschreibung Action .
 * Die Controller Klasse der Struts Model View Controller Architektur.
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 03.07.2004
 */
public final class SaveProjektbeschreibungAction extends Action {

    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log = LogFactory.getLog("org.fhw.cabaweb.webfrontend.actions.save");

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
        DataInterfaceProjektbeschreibungen dipb = new DataInterfaceProjektbeschreibungen();
        DataInterfaceProjekte dip = new DataInterfaceProjekte();
        DataInterfaceSprachen dis = new DataInterfaceSprachen();
        DataInterfaceGruppenmitglieder dig = new DataInterfaceGruppenmitglieder();
        MessageResources messages = getResources(request);
        HttpSession session = request.getSession();
        ProjektbeschreibungForm prjbform = (ProjektbeschreibungForm) form;
        String action = prjbform.getAction();
        boolean returnvalue = false;
        Projektbeschreibungen projektbeschreibung = null;
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
            action = "?";
        }
        if (log.isDebugEnabled()) {
            log.debug("SaveProjektbeschreibungAction: Processing " + action + " action");
            if (isAdmin) {
                log.debug(" User has Role: Administrator");
            } else if (isProjectleader) {
                log.debug(" User has Role: Projectleader");
            } else if (isUser) {
                log.debug(" User has Role: User");
            }
        }
        if (isCancelled(request)) {
            if (log.isDebugEnabled()) {
                log.debug(" Transaction '" + action + "' was cancelled");
            }
            session.removeAttribute(Keys.PROJEKTBESCHREIBUNG_KEY);
            return (mapping.findForward("success"));
        }
        projektbeschreibung = (Projektbeschreibungen) session.getAttribute(Keys.PROJEKTBESCHREIBUNG_KEY);
        if (projektbeschreibung == null) {
            if (log.isDebugEnabled()) {
                log.debug(" In Session saved Project Description is NULL.");
            }
            projektbeschreibung = new Projektbeschreibungen();
        }
        if (log.isDebugEnabled()) {
            log.debug(" Populating database from form bean");
        }
        try {
            PropertyUtils.copyProperties(projektbeschreibung, prjbform);
            projektbeschreibung.setProjekte((Projekte) dip.sucheProjektnummer(prjbform.getProjektnummer()));
            projektbeschreibung.setSprachen((Sprachen) dis.sucheSprachnummer(prjbform.getSprachnummer()));
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t == null) t = e;
            log.error("ProjektbeschreibungForm.populate", t);
            throw new ServletException("ProjektbeschreibungForm.populate", t);
        } catch (Throwable t) {
            log.error("ProjektbeschreibungForm.populate", t);
            throw new ServletException("ProjektbeschreibungForm.populate", t);
        }
        projektbeschreibung.setKurzbeschreibung(StringUtilities.convertUTF8String(projektbeschreibung.getKurzbeschreibung()));
        projektbeschreibung.setBeschreibung(StringUtilities.convertUTF8String(projektbeschreibung.getBeschreibung()));
        if ("Create".equals(action) && (isAdmin || isProjectleader)) {
            if (log.isDebugEnabled()) {
                log.debug("Creating a new Project Description: " + projektbeschreibung);
            }
            returnvalue = dipb.erzeugen(projektbeschreibung);
            session.removeAttribute(Keys.PROJEKTBESCHREIBUNG_KEY);
            if (!returnvalue) {
                if (log.isDebugEnabled()) {
                    log.debug(" Project Description creation failed");
                }
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, messages.getMessage("error.projektbeschreibung.create"));
                return (null);
            }
        }
        if (action.equals("Delete") && isAdmin) {
            if (log.isDebugEnabled()) {
                log.debug(" Deleting existing Project Description [ " + projektbeschreibung.getProjekte().getProjektname() + ", " + projektbeschreibung.getSprachen().getSprachname() + " ] " + projektbeschreibung.getKurzbeschreibung());
            }
            returnvalue = dipb.loeschen(projektbeschreibung);
            session.removeAttribute(Keys.PROJEKTBESCHREIBUNG_KEY);
            if (!returnvalue) {
                if (log.isDebugEnabled()) {
                    log.debug(" Project Description deletion failed");
                }
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, messages.getMessage("error.projektbeschreibung.delete"));
                return (null);
            }
        }
        if (action.equals("Edit") && (isAdmin || isProjectleader)) {
            if (log.isDebugEnabled()) {
                log.debug(" Editing existing Project Description [ " + projektbeschreibung.getProjekte().getProjektname() + ", " + projektbeschreibung.getSprachen().getSprachname() + " ] " + projektbeschreibung.getKurzbeschreibung());
            }
            Collection usernames = dig.sucheLogInName(username);
            Iterator iter = usernames.iterator();
            Gruppenmitglieder user = null;
            while (iter.hasNext()) {
                user = (Gruppenmitglieder) iter.next();
            }
            if (isProjectleader && projektbeschreibung.getProjekte().getProjektnummer().equals(((user.getProjektgruppe()).getProjekte()).getProjektnummer()) || isAdmin) {
                if (log.isDebugEnabled()) {
                    log.debug(" Project Description editing is being processed");
                }
                returnvalue = dipb.editieren(projektbeschreibung);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(" User not authorized to edit Project Description");
                }
            }
            session.removeAttribute(Keys.PROJEKTBESCHREIBUNG_KEY);
            if (!returnvalue) {
                if (log.isDebugEnabled()) {
                    log.debug(" Project Description editing failed");
                }
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, messages.getMessage("error.projektbeschreibung.edit"));
                return (null);
            }
        }
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) request.removeAttribute(mapping.getAttribute()); else session.removeAttribute(mapping.getAttribute());
        }
        session.removeAttribute(Keys.PROJEKTBESCHREIBUNG_KEY);
        if (log.isDebugEnabled()) {
            log.debug(" Forwarding to 'success' page");
        }
        return (mapping.findForward("success"));
    }
}
