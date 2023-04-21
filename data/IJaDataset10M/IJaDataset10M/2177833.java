package seismosurfer.data;

import java.io.Serializable;

/**
 * A Data Tranfer Object that corresponds to the <code>QUAKE_MAG_CATALOG</code> view
 * and contains data from the <code>QUAKE</code>, <code>MAG</code> and <code>CATALOG</code>
 * tables.
 */
public class QuakeData implements Serializable {

    private static final long serialVersionUID = -3986819104577592006L;

    private long quakeID = 0;

    private boolean hasMacroseismic = false;

    private boolean hasInfo = false;

    private long dateTime = 0;

    private double longitude = 0;

    private double latitude = 0;

    private String catalogName = "";

    private int catalogID = 0;

    private String agency = "";

    private double depth = 0;

    private double magnitude = 0;

    private int FECode = 0;

    private String source;

    private long loadDate;

    /**
     */
    public QuakeData() {
    }

    /**
     */
    public int getFECode() {
        return FECode;
    }

    /**
     */
    public void setFECode(int code) {
        FECode = code;
    }

    /**
     */
    public int getCatalogID() {
        return catalogID;
    }

    /**
     */
    public void setCatalogID(int catID) {
        catalogID = catID;
    }

    /**
     */
    public long getQuakeID() {
        return this.quakeID;
    }

    /**
     */
    public boolean getHasMacroseismic() {
        return this.hasMacroseismic;
    }

    /**
     */
    public boolean getHasInfo() {
        return this.hasInfo;
    }

    /**
     */
    public String getAgency() {
        return this.agency;
    }

    /**
     */
    public String getCatalogName() {
        return this.catalogName;
    }

    /**
     */
    public double getMagnitude() {
        return this.magnitude;
    }

    /**
     */
    public double getDepth() {
        return this.depth;
    }

    /**
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     */
    public long getDateTime() {
        return this.dateTime;
    }

    /**
     */
    public void setQuakeID(long quakeID) {
        this.quakeID = quakeID;
    }

    /**
     */
    public void setAgency(String agency) {
        this.agency = agency;
    }

    /**
     */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     */
    public void setHasMacroseismic(boolean has) {
        this.hasMacroseismic = has;
    }

    /**
     */
    public void setHasInfo(boolean has) {
        this.hasInfo = has;
    }

    /**
     */
    public void setMagnitude(double mag) {
        this.magnitude = mag;
    }

    /**
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     */
    public void setDateTime(long time) {
        this.dateTime = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(long loadDate) {
        this.loadDate = loadDate;
    }

    public String toString() {
        return "[" + getAgency() + ", " + getCatalogID() + ", " + getCatalogName() + ", " + getDateTime() + ", " + getDepth() + ", " + getFECode() + ", " + getSource() + "]";
    }
}
