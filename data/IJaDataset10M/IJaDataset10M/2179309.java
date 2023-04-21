package org.ibit.avanthotel.end.web.util;

import org.ibit.avanthotel.persistence.logic.data.consulta.ConsultaData;

/**
 * @created 7 de octubre de 2003
 */
public class InfoSessio {

    private String lang;

    private ConsultaData consulta;

    private int pag;

    private Integer serveisTemp[];

    private boolean haFetCerca;

    private Boolean teVisorFlash;

    /**Construeix una nova instancia de InfoSessio */
    public InfoSessio() {
        this.lang = Constants.ATRIBUT_LANG_DEFECTE;
        pag = 1;
        this.haFetCerca = false;
    }

    /**
    * retorna el valor per la propietat lang
    *
    * @return valor de lang
    */
    public String getLang() {
        return lang;
    }

    /**
    * fitxa el valor de lang
    *
    * @param lang nou valor per lang
    */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
    * retorna el valor per la propietat consulta
    *
    * @return valor de consulta
    */
    public ConsultaData getConsulta() {
        return consulta;
    }

    /**
    * fitxa el valor de consulta
    *
    * @param consulta nou valor per consulta
    */
    public void setConsulta(ConsultaData consulta) {
        this.consulta = consulta;
    }

    /**
    * retorna el valor per la propietat pag
    *
    * @return valor de pag
    */
    public int getPag() {
        return pag;
    }

    /**
    * fitxa el valor de pag
    *
    * @param pag nou valor per pag
    */
    public void setPag(String pag) {
        this.pag = Integer.parseInt(pag);
    }

    /**
    * fitxa el valor de pag
    *
    * @param pag nou valor per pag
    */
    public void setPag(int pag) {
        this.pag = pag;
    }

    /**
    * retorna el valor per la propietat serveisTemp
    *
    * @return valor de serveisTemp
    */
    public Integer[] getServeisTemp() {
        return serveisTemp;
    }

    /**
    * fitxa el valor de serveisTemp
    *
    * @param serveisTemp nou valor per serveisTemp
    */
    public void setServeisTemp(Integer[] serveisTemp) {
        this.serveisTemp = serveisTemp;
    }

    /**
    * retorna el valor per la propietat haFetCerca
    *
    * @return valor de haFetCerca
    */
    public boolean isHaFetCerca() {
        return haFetCerca;
    }

    /** fitxa el valor de haFetCerca */
    public void setHaFetCerca() {
        this.haFetCerca = true;
    }

    /**
    * retorna el valor per la propietat teVisorFlash
    *
    * @return valor de teVisorFlash
    */
    public Boolean getTeVisorFlash() {
        return teVisorFlash;
    }

    /**
    * fitxa el valor de teVisorFlash
    *
    * @param teVisorFlash nou valor per teVisorFlash
    */
    public void setTeVisorFlash(Boolean teVisorFlash) {
        this.teVisorFlash = teVisorFlash;
    }
}
