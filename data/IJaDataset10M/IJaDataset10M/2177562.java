package spidr.swr;

import java.io.*;
import spidr.datamodel.*;
import spidr.swr.*;

/** This class stores station information. */
public class SwrStation extends Station implements Serializable {

    /** Station source (WDC, 210mm, etc.) */
    protected String source;

    /** Station dataType (HDZ, XYZ, etc.) */
    protected String dataType;

    /** Station magnetic latitude*/
    protected float maglat = BADVALUE;

    /** Station magnetic longitude*/
    protected float maglon = BADVALUE;

    /** Station meridian time */
    protected float declination = BADVALUE;

    /** Constructs object with unknown initial values.   *   code=dataTable=name=""; lat=lon=declination=BADVALUE;   */
    public SwrStation() {
        reset("", "", "", BADVALUE, BADVALUE, (byte) 0);
    }

    /** Constructs the object with unknown location.   *   lat=lon=declination=BADVALUE;   */
    public SwrStation(String code, String table, String name) {
        reset(code, table, name, BADVALUE, BADVALUE, (byte) 0);
    }

    /** Constructs the object with unknown declination   */
    public SwrStation(String code, String table, String name, float lat, float lon) {
        reset(code, table, name, lat, lon, (byte) 0);
    }

    /** Constructs the object with coordinates and declination   */
    public SwrStation(String code, String table, String name, float lat, float lon, float declination) {
        reset(code, table, name, lat, lon, (byte) 0);
        this.declination = declination;
    }

    /** Returns station data source.   * @return station source   */
    public String getSource() {
        return source;
    }

    /** Station data source setter.   */
    public void setSource(String source) {
        this.source = source;
    }

    /** Returns station dataType.   * @return station dataType code   */
    public String getDataType() {
        return dataType;
    }

    /** Station dataType setter.   */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /** Returns station magnetic latitude.   * @return Station magnetic latitude   */
    public float getMaglat() {
        return maglat;
    }

    /** Station magnetic latitude setter.   */
    public void setMaglat(float maglat) {
        this.maglat = maglat;
    }

    /** Return station longitude   * @return Station longitude   */
    public float getMaglon() {
        return maglon;
    }

    /** Station magnetic longitude setter.   */
    public void setMaglon(float maglon) {
        this.maglon = maglon;
    }

    /** Return station declination   * @return Station declination   */
    public float getDeclination() {
        return declination;
    }

    /** Station magnetic declination setter.   */
    public void setDeclination(float declination) {
        this.declination = declination;
    }

    public boolean equals(Object st) {
        if (st == null) {
            return false;
        } else {
            return (getSType().equals(((SwrStation) st).getSType()) && getStn().equals(((SwrStation) st).getStn()));
        }
    }

    public void print() {
        System.out.println(this.toString());
    }

    public String toString() {
        String resString = "SWR Station [" + getStn() + "] Info: \n";
        resString += "  Name=" + getName() + " Table=" + getSType() + " Type=" + dataType + " Source=" + source + "\n";
        resString += "  Lat=" + getLat() + " Lon=" + getLon() + "\n";
        resString += "  Maglat=" + maglat + " Maglon=" + maglon + "\n";
        resString += "  Declination=" + declination + " Meridian Time=" + getMeridianTime();
        return resString;
    }
}
