package net.sf.magicmap.db;

/**
 * @author lederer & wei�
 * 
 * @jdo.persistence-capable 
 *   identity-type="application"
 * @jdo.class-vendor-extension 
 *   vendor-name="jpox" 
 *   key="table-name"
 *   value="geoposition"
 * @jdo.class-vendor-extension 
 *   vendor-name="jpox" key="use-poid-generator"
 *   value="true"
 * @jdo.class-vendor-extension 
 *   vendor-name="jpox" key="poid-class-generator"
 *   value="org.jpox.poid.SequenceTablePoidGenerator"
 */
public class GeoPosition {

    /**
     *
     */
    protected GeoPosition() {
        super();
    }

    /**
     * primary key
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   primary-key="true"
     */
    long id;

    /**
     * zugeh�rige Karte
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    Map map;

    /**
     * x Position im Bild
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    Integer posX;

    /**
     * y Position im Bild
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    Integer posY;

    /**
     * Latitude der GeoPosition
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    Integer latitude;

    /**
     * Longgitude der GeoPosition
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    Integer longitude;

    /**
     * Altitude der GeoPosition
     * 
     * @jdo.field 
     *   persistence-modifier="persistent"
     *   null-value="exception"
     */
    Integer altitude;

    /**
     * @param map
     * @param posX
     * @param posY
     * @param latitude
     * @param longitude
     * @param altitude
     */
    public GeoPosition(Map map, Integer posX, Integer posY, Integer longitude, Integer latitude, Integer altitude) {
        super();
        this.map = map;
        this.posX = posX;
        this.posY = posY;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * @return Returns the id.
     */
    public long getId() {
        return this.id;
    }

    /**
     * @return Returns the map.
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * @return Returns the posX.
     */
    public Integer getPosX() {
        return this.posX;
    }

    /**
     * @return Returns the posY.
     */
    public Integer getPosY() {
        return this.posY;
    }

    /**
     * @return Returns the latitude.
     */
    public Integer getLatitude() {
        return latitude;
    }

    /**
     * @return Returns the longitude.
     */
    public Integer getLongitude() {
        return longitude;
    }

    /**
     * @return Returns the altitude.
     */
    public Integer getAltitude() {
        return altitude;
    }

    /**
     * @param altitude The altitude to set.
     */
    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    /**
     * @param latitude The latitude to set.
     */
    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    /**
     * @param longitude The longitude to set.
     */
    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    /**
     * @param map The map to set.
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * @param posX The posX to set.
     */
    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    /**
     * @param posY The posY to set.
     */
    public void setPosY(Integer posY) {
        this.posY = posY;
    }
}
