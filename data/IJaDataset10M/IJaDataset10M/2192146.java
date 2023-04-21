package seismosurfer.data;

/**
 * A Data Transfer Object to represent duplicate earthquake data, that are 
 * found in the database during update.
 *
 */
public class DuplicateData extends DataTransferObject {

    private static final long serialVersionUID = -3020779948264613325L;

    private long quakeID;

    private int catalogID;

    private boolean hasMacro;

    private boolean hasInfo;

    public DuplicateData() {
    }

    public long getQuakeID() {
        return quakeID;
    }

    public void setQuakeID(long quakeID) {
        this.quakeID = quakeID;
    }

    public int getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(int catalogID) {
        this.catalogID = catalogID;
    }

    public boolean getMacro() {
        return hasMacro;
    }

    public void setMacro(boolean hasMacro) {
        this.hasMacro = hasMacro;
    }

    public boolean getInfo() {
        return hasInfo;
    }

    public void setInfo(boolean hasInfo) {
        this.hasInfo = hasInfo;
    }
}
