package org.isportal.db.tables;

import java.util.*;

/**
 * 
 * @author Mitja Turnšek 
 * e-Mail : Mitja.Turnsek@gmail.com
 * @version 1.0
 * Datum: 21.11.2005
 * Datoteka: Novice.java
 * Paket: org.isportal.db.tables
 */
public class Novice {

    private Long idNovice;

    private String naslov;

    private String kratekOpis;

    private String vsebina;

    private Date datumVnosa;

    private Date datumPrikaza;

    private Integer idUporabnik;

    private String opombe;

    private String RSS;

    private Kategorija kategorija;

    private Boolean aktiven;

    private String avtor;

    /**
     * @return Returns the datumPrikaza.
     */
    public Date getDatumPrikaza() {
        return datumPrikaza;
    }

    /**
     * @param datumPrikaza The datumPrikaza to set.
     */
    public void setDatumPrikaza(Date datumPrikaza) {
        this.datumPrikaza = datumPrikaza;
    }

    /**
     * @return Returns the datumVnosa.
     */
    public Date getDatumVnosa() {
        return datumVnosa;
    }

    /**
     * @param datumVnosa The datumVnosa to set.
     */
    public void setDatumVnosa(Date datumVnosa) {
        this.datumVnosa = datumVnosa;
    }

    /**
     * @return Returns the idNovice.
     */
    public Long getIdNovice() {
        return idNovice;
    }

    /**
     * @param idNovice The idNovice to set.
     */
    public void setIdNovice(Long idNovice) {
        this.idNovice = idNovice;
    }

    /**
     * @return Returns the idUporabnik.
     */
    public Integer getIdUporabnik() {
        return idUporabnik;
    }

    /**
     * @param idUporabnik The idUporabnik to set.
     */
    public void setIdUporabnik(Integer idUporabnik) {
        this.idUporabnik = idUporabnik;
    }

    /**
     * @return Returns the kategorija.
     */
    public Kategorija getKategorija() {
        return kategorija;
    }

    /**
     * @param kategorija The kategorija to set.
     */
    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    /**
     * @return Returns the kratekOpis.
     */
    public String getKratekOpis() {
        return kratekOpis;
    }

    /**
     * @param kratekOpis The kratekOpis to set.
     */
    public void setKratekOpis(String kratekOpis) {
        this.kratekOpis = kratekOpis;
    }

    /**
     * @return Returns the naslov.
     */
    public String getNaslov() {
        return naslov;
    }

    /**
     * @param naslov The naslov to set.
     */
    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    /**
     * @return Returns the opombe.
     */
    public String getOpombe() {
        return opombe;
    }

    /**
     * @param opombe The opombe to set.
     */
    public void setOpombe(String opombe) {
        this.opombe = opombe;
    }

    /**
     * @return Returns the rSS.
     */
    public String getRSS() {
        return RSS;
    }

    /**
     * @param rss The rSS to set.
     */
    public void setRSS(String rss) {
        RSS = rss;
    }

    /**
     * @return Returns the vsebina.
     */
    public String getVsebina() {
        return vsebina;
    }

    /**
     * @param vsebina The vsebina to set.
     */
    public void setVsebina(String vsebina) {
        this.vsebina = vsebina;
    }

    /**
     * @return Returns the aktiven.
     */
    public Boolean getAktiven() {
        return aktiven;
    }

    /**
     * @param aktiven The aktiven to set.
     */
    public void setAktiven(Boolean aktiven) {
        this.aktiven = aktiven;
    }

    /**
     * @return Returns the avtor.
     */
    public String getAvtor() {
        return avtor;
    }

    /**
     * @param avtor The avtor to set.
     */
    public void setAvtor(String avtor) {
        this.avtor = avtor;
    }
}
