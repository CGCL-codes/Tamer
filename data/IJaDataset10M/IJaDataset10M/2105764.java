package org.fhw.cabaweb.webfrontend.forms.multiple;

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Formularklasse der MVC Architektur von Struts.
 * In diesem Fall f�r eine Collection von Objekten (Liste der Voreinstellungen Feldnamen)
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 14.07.2004
 */
public final class VoreinstellungenFeldnamenForm extends ActionForm {

    /** Collection von Gruppenmitglieder Objekten */
    private Collection voreinstellungenFeldnamen = null;

    /** 
     * GET Methode 
     *
     * @return der Parameterwert (Collection von Voreinstellungen Feldnamen) 
     */
    public Collection getVoreinstellungenFeldnamen() {
        return this.voreinstellungenFeldnamen;
    }

    /**
     * SET Methode 
     *
     * @param voreinstellungenFeldnamen Der zu setzende Parameterwert (Collection von Voreinstellungen Feldnamen)
     */
    public void setVoreinstellungenFeldnamen(Collection voreinstellungenFeldnamen) {
        this.voreinstellungenFeldnamen = voreinstellungenFeldnamen;
    }

    /**
     * Zur�cksetzen aller Parameterwerte auf die Default Werte.
     *
     * @param mapping Das Mapping das benutzt wurde um diese Instanz zu selektieren
     * @param request Die Servlet Anfrage die wir gerade bearbeiten
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.voreinstellungenFeldnamen = null;
    }
}
