package org.fhw.cabaweb.ojb.dataobjects;

import java.io.Serializable;

/**
 * Klasse f&uuml;r die Datenbankrepr&auml;sentation der Berechnungsauftr�ge
 *
 * @author  <a href="mailto:thomas.vogt@tvc-software.com">Thomas Vogt</a>
 * @version Version 1.0 05.05.2004
 */
public class Voreinstellungen_Untergruppierungsnamen implements Serializable {

    /** Artificial PrimaryKey f�r die Identifikation der Untergruppierung */
    private Integer untergruppierungsnummer;

    /** Foreign PrimaryKey f�r die Identifikation der Gruppierung */
    private Voreinstellungen_Gruppierungsnamen voreinstellungen_gruppierungsnamen;

    /** Der Name der Untergruppierung */
    private String untergruppierungsname;

    /**
     * Standardkonstruktor
     */
    public Voreinstellungen_Untergruppierungsnamen() {
        this.setUntergruppierungsnummer(null);
        this.setVoreinstellungen_gruppierungsnamen(null);
        this.setUntergruppierungsname(null);
    }

    /**
     * &Uuml;berschriebener Konstruktor mit alle Parametern als �bergabeparamerter
     *
     * @param untergruppierungsnummer    Der Wert f�r den Parameter Untergruppierungsnummer
     * @param voreinstellungen_gruppierungsnamen	Der Wert f�r den Parameter Voreinstellungen_Gruppierungsnamen
     * @param untergruppierungsname Der Name der Untergruppierung
     */
    public Voreinstellungen_Untergruppierungsnamen(Integer untergruppierungsnummer, Voreinstellungen_Gruppierungsnamen voreinstellungen_gruppierungsnamen, String untergruppierungsname) {
        this.setUntergruppierungsnummer(untergruppierungsnummer);
        this.setVoreinstellungen_gruppierungsnamen(voreinstellungen_gruppierungsnamen);
        this.setUntergruppierungsname(untergruppierungsname);
    }

    /**
     * gets voreinstellungen_gruppierungsnamen
     * @return voreinstellungen_gruppierungsnamen 
     */
    public Voreinstellungen_Gruppierungsnamen getVoreinstellungen_gruppierungsnamen() {
        return voreinstellungen_gruppierungsnamen;
    }

    /**
     * sets voreinstellungen_gruppierungsnamen
     * @param voreinstellungen_gruppierungsnamen voreinstellungen_gruppierungsnamen 
     */
    public void setVoreinstellungen_gruppierungsnamen(Voreinstellungen_Gruppierungsnamen voreinstellungen_gruppierungsnamen) {
        this.voreinstellungen_gruppierungsnamen = voreinstellungen_gruppierungsnamen;
    }

    /**
     * gets untergruppierungsnummer
     * @return untergruppierungsnummer 
     */
    public Integer getUntergruppierungsnummer() {
        return untergruppierungsnummer;
    }

    /**
     * sets untergruppierungsnummer
     * @param untergruppierungsnummer untergruppierungsnummer 
     */
    public void setUntergruppierungsnummer(Integer untergruppierungsnummer) {
        this.untergruppierungsnummer = untergruppierungsnummer;
    }

    /**
     * gets untergruppierungsname
     * @return untergruppierungsname 
     */
    public String getUntergruppierungsname() {
        return untergruppierungsname;
    }

    /**
     * sets untergruppierungsname
     * @param untergruppierungsname untergruppierungsname 
     */
    public void setUntergruppierungsname(String untergruppierungsname) {
        this.untergruppierungsname = untergruppierungsname;
    }

    /**
     * Gibt den Inhalt der Klassenparameter als String zur&uuml;ck
     *
     * @return String mit dem Inhalt der Klassenparameter
     */
    public String toString() {
        return "[ Gruppierung " + voreinstellungen_gruppierungsnamen.getGruppierungsname() + " Projekt " + voreinstellungen_gruppierungsnamen.getProjekte().getProjektname() + " ] Untergruppierungsnummer: " + untergruppierungsnummer + " Untergruppierungsname: " + untergruppierungsname;
    }
}
