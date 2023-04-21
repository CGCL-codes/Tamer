package org.fhw.cabaweb.webfrontend.actions.save;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fhw.cabaweb.data.DataInterfaceErgebnissdaten;
import org.fhw.cabaweb.data.DataInterfaceErgebnissdatenGruppierungsnamen;
import org.fhw.cabaweb.data.DataInterfaceGruppenmitglieder;
import org.fhw.cabaweb.data.constants.Constants;
import org.fhw.cabaweb.data.dataobjects.Ergebniss;
import org.fhw.cabaweb.ojb.dataobjects.Ergebnissdaten_Gruppierungsnamen;
import org.fhw.cabaweb.ojb.dataobjects.Gruppenmitglieder;
import org.fhw.cabaweb.webfrontend.configs.Keys;

/**
 * <strong>Action</strong>-Klasse f&uuml;r die Save Entscheidung Action .
 * Die Controller Klasse der Struts Model View Controller Architektur.
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 28.07.2004
 */
public final class SaveEntscheidungAction extends Action {

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
        DataInterfaceErgebnissdatenGruppierungsnamen diegn = new DataInterfaceErgebnissdatenGruppierungsnamen();
        DataInterfaceGruppenmitglieder digm = new DataInterfaceGruppenmitglieder();
        DataInterfaceErgebnissdaten die = new DataInterfaceErgebnissdaten();
        MessageResources messages = getResources(request, "EntscheidungenResources");
        HttpSession session = request.getSession();
        DynaValidatorForm dvform = (DynaValidatorForm) form;
        String action = (String) dvform.get("action");
        boolean returnvalue = false;
        Collection entscheidungen = null;
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
            log.debug("SaveEntscheidungAction: Processing " + action + " action");
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
            session.removeAttribute(Keys.ENTSCHEIDUNGEN_KEY);
            return (mapping.findForward("success"));
        }
        Collection usernames = digm.sucheLogInName(username);
        Iterator iter = usernames.iterator();
        Gruppenmitglieder user = null;
        if (iter.hasNext()) {
            user = (Gruppenmitglieder) iter.next();
            if (log.isDebugEnabled()) {
                log.debug("SaveEntscheidungAction: User '" + user.toString() + "'");
            }
        }
        Integer projektnummer = user.getProjektgruppe().getProjekte().getProjektnummer();
        Integer gruppennummer = user.getProjektgruppe().getGruppennummer();
        Integer quartal = user.getProjektgruppe().getProjekte().getAktuellesQuartal();
        entscheidungen = (Collection) session.getAttribute(Keys.ENTSCHEIDUNGEN_KEY);
        if (entscheidungen == null) {
            if (log.isDebugEnabled()) {
                log.debug(" In Session saved Entscheidung is NULL.");
            }
            if (log.isDebugEnabled()) {
                log.debug(" Populating database from form bean");
            }
        }
        Collection gruppierungsnamen = diegn.sucheKombination(null, projektnummer, Constants.EG_ENTSCHEIDUNGEN_KEY);
        iter = gruppierungsnamen.iterator();
        Ergebnissdaten_Gruppierungsnamen gruppierungsname = null;
        Integer gruppierungsnummer = null;
        if (iter.hasNext()) {
            gruppierungsname = (Ergebnissdaten_Gruppierungsnamen) iter.next();
            gruppierungsnummer = gruppierungsname.getGruppierungsnummer();
            if (log.isDebugEnabled()) {
                log.debug(" Group Nr. '" + gruppierungsname.getGruppierungsnummer() + "'");
            }
        }
        entscheidungen = die.sucheKombination_Gruppierung(projektnummer, gruppennummer, new Integer(quartal.intValue() + 1), gruppierungsnummer);
        Iterator entscheidungsiterator = entscheidungen.iterator();
        while (entscheidungsiterator.hasNext()) {
            Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
            if (entscheidung == null) {
                entscheidungsiterator.remove();
            }
        }
        try {
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung == null) {
                    entscheidungsiterator.remove();
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGVERKAUFSPREIS_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("preis '" + dvform.get("preis") + "'");
                    }
                    entscheidung.setObjectWert(new Double((String) dvform.get("preis")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGMARKETING_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("marketing '" + dvform.get("marketing") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("marketing")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGMARKTFORSCHUNGSDIENST_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("marktforschung '" + dvform.get("marktforschung") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("marktforschung")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGABSETZBAREMENGE_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("absetzbaremenge '" + dvform.get("absetzbaremenge") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("absetzbaremenge")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGPRODUKTART_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("produktart '" + dvform.get("produktart") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("produktart")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGPRODUKTIONSMENGE_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("produktionsmenge '" + dvform.get("produktionsmenge") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("produktionsmenge")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGQUALITAETSSICHERUNG_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("qualitaetssicherung '" + dvform.get("qualitaetssicherung") + "'");
                    }
                    entscheidung.setObjectWert(new Double((String) dvform.get("qualitaetssicherung")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGQSKOSTENMINIMUM_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("qsoptimal '" + dvform.get("qsoptimal") + "'");
                    }
                    entscheidung.setObjectWert(new Double((String) dvform.get("qsoptimal")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGKAPAZITAETSAUSLASTUNG_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("kapazitaetsauslastung '" + dvform.get("kapazitaetsauslastung") + "'");
                    }
                    entscheidung.setObjectWert(new Double((String) dvform.get("kapazitaetsauslastung")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGINVESTITIONEN_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("investition '" + dvform.get("investition") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("investition")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGROHSTOFFBESTELLUNGNORMAL_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("rohstoffnormal " + dvform.get("rohstoffnormal") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("rohstoffnormal")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGROHSTOFFBESTELLUNGEXPRESS_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("rohstoffexpress '" + dvform.get("rohstoffexpress") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("rohstoffexpress")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGDIVIDENDE_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("dividende '" + dvform.get("dividende") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("dividende")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGAENDERUNGBANKDARLEHEN_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("aenderungvereinbarterkredit '" + dvform.get("aenderungvereinbarterkredit") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("aenderungvereinbarterkredit")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGERHOEHUNGTILGUNG_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("erhoehungoderverminderung '" + dvform.get("erhoehungoderverminderung") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("erhoehungoderverminderung")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGZINSEN_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("zinsen '" + dvform.get("zinsen") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("zinsen")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGLIQUIDITAETSAENDERUNG_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("liquiditaetsaenderung '" + dvform.get("liquiditaetsaenderung") + "'");
                    }
                    entscheidung.setObjectWert(new Integer(((String) dvform.get("liquiditaetsaenderung")).replaceAll("\\+", "")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGFUNDE_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("forschung '" + dvform.get("forschung") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("forschung")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGGRENZGEWINN_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("grenzgewinn '" + dvform.get("grenzgewinn") + "'");
                    }
                    entscheidung.setObjectWert(new Double((String) dvform.get("grenzgewinn")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGLIZENZVERKAUFUNTERNEHMEN_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("lizenzverkaufan '" + dvform.get("lizenzverkaufan") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("lizenzverkaufan")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGLIZENZVERKAUFPRODUKTART_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("lizenzverkaufproduktart '" + dvform.get("lizenzverkaufproduktart") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("lizenzverkaufproduktart")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGLIZENZKAUFUNTERNEHMEN_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("lizenzkaufvon '" + dvform.get("lizenzkaufvon") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("lizenzkaufvon")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGLIZENZKAUFFUER_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("lizenzkaufkosten '" + dvform.get("lizenzkaufkosten") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("lizenzkaufkosten")));
                    break;
                }
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (entscheidung.getFeldname().compareTo(Constants.EF_ENTSCHEIDUNGBERATUNG_KEY) == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("beratung '" + dvform.get("beratung") + "'");
                    }
                    entscheidung.setObjectWert(new Integer((String) dvform.get("beratung")));
                    break;
                }
            }
        } catch (Throwable t) {
            log.error(" Error in DataTransfer from Form", t);
        }
        if ("Create".equals(action) || action.equals("Edit")) {
            if (log.isDebugEnabled()) {
                log.debug("Creating a new Entscheidung");
            }
            entscheidungsiterator = entscheidungen.iterator();
            while (entscheidungsiterator.hasNext()) {
                Ergebniss entscheidung = (Ergebniss) entscheidungsiterator.next();
                if (log.isDebugEnabled()) {
                    log.debug(" --");
                    log.debug(" Feldnummer: " + entscheidung.getFeldnummer());
                    log.debug(" Gruppennummer: " + entscheidung.getGruppennummer());
                    log.debug(" Quartal: " + entscheidung.getQuartal());
                    log.debug(" Wert: " + entscheidung.getObjectWert().toString());
                    log.debug(" --");
                }
                returnvalue = die.editieren(entscheidung.getFeldnummer(), entscheidung.getGruppennummer(), entscheidung.getQuartal(), entscheidung.getObjectWert().toString());
                if (!returnvalue) {
                    if (log.isDebugEnabled()) {
                        log.debug(" Entscheidung saving failed");
                    }
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, messages.getMessage("error.benutzerrolle.create"));
                    return (null);
                }
            }
            session.removeAttribute(Keys.BENUTZERROLLE_KEY);
        } else {
            if (log.isDebugEnabled()) {
                log.debug(" User not authorized to edit Entscheidung");
            }
        }
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) request.removeAttribute(mapping.getAttribute()); else session.removeAttribute(mapping.getAttribute());
        }
        session.removeAttribute(Keys.BENUTZERROLLE_KEY);
        if (log.isDebugEnabled()) {
            log.debug(" Forwarding to 'success' page");
        }
        return (mapping.findForward("success"));
    }
}
