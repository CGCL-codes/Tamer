package org.ibit.avanthotel.persistence.logic.data.area;

/**
 * Estructura que guarda los parametros d'informacio de relacio entre arees i flash.
 *
 * @created 8 de juliol de 2003
 */
public class FlashAreesData implements java.io.Serializable {

    private String idAreaFlash;

    private String idArea;

    private String flash;

    private String isZona;

    /**
    *Construeix una nova instancia de FlashAreesData
    *
    * @param idAreaFlash
    * @param idArea
    * @param flash
    * @param isZona
    */
    public FlashAreesData(String idAreaFlash, String idArea, String flash, String isZona) {
        this.idAreaFlash = idAreaFlash;
        this.idArea = idArea;
        this.flash = flash;
        this.isZona = isZona;
    }

    /**
    * retorna el valor per la propietat idAreaFlash
    *
    * @return valor de idAreaFlash
    */
    public String getIdAreaFlash() {
        return idAreaFlash;
    }

    /**
    * fitxa el valor de idAreaFlash
    *
    * @param idAreaFlash nou valor per idAreaFlash
    */
    public void setIdAreaFlash(String idAreaFlash) {
        this.idAreaFlash = idAreaFlash;
    }

    /**
    * retorna el valor per la propietat idArea
    *
    * @return valor de idArea
    */
    public String getIdArea() {
        return idArea;
    }

    /**
    * fitxa el valor de idArea
    *
    * @param idArea nou valor per idArea
    */
    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    /**
    * retorna el valor per la propietat flash
    *
    * @return valor de flash
    */
    public String getFlash() {
        return flash;
    }

    /**
    * fitxa el valor de flash
    *
    * @param flash nou valor per flash
    */
    public void setFlash(String flash) {
        this.flash = flash;
    }

    /**
    * retorna el valor per la propietat isZona
    *
    * @return valor de isZona
    */
    public String getIsZona() {
        return isZona;
    }

    /**
    * fitxa el valor de isZona
    *
    * @param isZona nou valor per isZona
    */
    public void setIsZona(String isZona) {
        this.isZona = isZona;
    }
}
