package org.fhw.cabaweb.webfrontend.forms.simple;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Formularklasse der MVC Architektur von Struts.
 * In diesem Fall f�r ein einzelnes Projekt (Create/Delete/Edit eines Projekts)
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 03.06.2004
 */
public final class ProjektForm extends ActionForm {

    /**
     * The <code>Log</code> instance for this application.
     */
    private Log log = LogFactory.getLog("org.fhw.cabaweb.webfrontend.forms.simple");

    /** Art der Aktion die wir durchf�hren wollen (Create, Delete or Edit) - Default ist Create. */
    private String action = "Create";

    /** Die Projektnummer */
    private Integer projektnummer = null;

    /** Der Projektname */
    private String projektname = null;

    /** Das aktuelle Quartal */
    private Integer aktuellesQuartal = null;

    /** Ist das Projekt aktiv ? */
    private Boolean aktiv = null;

    /** 
     * GET Methode 
     *
     * @return der Parameterwert 
     */
    public String getAction() {
        return this.action;
    }

    /** 
     * SET Methode 
     *
     * @param action Der zu setzende Parameterwert 
     */
    public void setAction(String action) {
        this.action = action;
    }

    /** 
     * GET Methode 
     *
     * @return der Parameterwert 
     */
    public Integer getProjektnummer() {
        return this.projektnummer;
    }

    /** 
     * GET Methode 
     *
     * @return der Parameterwert 
     */
    public String getProjektname() {
        return this.projektname;
    }

    /** 
     * GET Methode 
     *
     * @return der Parameterwert 
     */
    public Integer getAktuellesQuartal() {
        return this.aktuellesQuartal;
    }

    /** 
     * GET Methode 
     *
     * @return der Parameterwert 
     */
    public Boolean getAktiv() {
        return this.aktiv;
    }

    /** 
     * SET Methode 
     *
     * @param projektnummer Der zu setzende Parameterwert 
     */
    public void setProjektnummer(Integer projektnummer) {
        this.projektnummer = projektnummer;
    }

    /** 
     * SET Methode 
     *
     * @param projektname Der zu setzende Parameterwert 
     */
    public void setProjektname(String projektname) {
        this.projektname = projektname;
    }

    /**
     * SET Methode 
     *
     * @param aktuellesQuartal Der zu setzende Parameterwert 
     */
    public void setAktuellesQuartal(Integer aktuellesQuartal) {
        this.aktuellesQuartal = aktuellesQuartal;
    }

    /** 
     * SET Methode 
     *
     * @param aktiv Der zu setzende Parameterwert 
     */
    public void setAktiv(Boolean aktiv) {
        this.aktiv = aktiv;
    }

    /**
     * Zur�cksetzen aller Parameterwerte auf die Default Werte.
     *
     * @param mapping Das Mapping das benutzt wurde um diese Instanz zu selektieren
     * @param request Die Servlet Anfrage die wir gerade bearbeiten
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.action = "Create";
        this.projektnummer = null;
        this.projektname = null;
        this.aktuellesQuartal = null;
        this.aktiv = null;
    }

    /**
     * Validieren der mit diesem Request �bergebenen Paramter Werte. Wenn Fehler
     * bei der Validierung auftreten wird <code>ActionErrors</code> Objekt, 
     * das die Fehler enth�lt zur�ckgegeben.
     * Wenn kein Fehler bei der Validierung auftritt wird <code>null</code> bzw.
     * ein leeres <code>ActionErrors</code> Objekt zur�ckgegeben
     *
     * @param mapping Das Mapping das benutzt wurde um diese Instanz zu selektieren 
     *                (siehe struts-config.xml)
     * @param request Das Servlet Anfrage Objekt
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (projektname == null || projektname.length() <= 0) errors.add("projektname", new ActionError("error.field.edit.projekt.projektname.empty")); else if (projektname.length() < 5) errors.add("projektname", new ActionError("error.field.edit.projekt.projektname.minlength")); else if (projektname.length() > 20) errors.add("projektname", new ActionError("error.field.edit.projekt.projektname.maxlength"));
        if (aktuellesQuartal == null) errors.add("aktuellesQuartal", new ActionError("error.field.edit.projekt.aktuellesQuartal.empty")); else if (aktuellesQuartal.intValue() < -1) errors.add("aktuellesQuartal", new ActionError("error.field.edit.projekt.aktuellesQuartal.min")); else if (aktuellesQuartal.intValue() > 20) errors.add("aktuellesQuartal", new ActionError("error.field.edit.projekt.aktuellesQuartal.max"));
        if (log.isDebugEnabled()) {
            Iterator iter = errors.get();
            log.debug(" Form had errors:");
            while (iter.hasNext()) {
                log.debug(" " + ((ActionError) iter.next()).getKey());
            }
        }
        return errors;
    }
}
