package net.sf.openv4j;

import java.util.Date;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class ErrorListEntry {

    private Date tinestamp;

    private int errorCode;

    /**
     * Creates a new ErrorListEntry object.
     *
     * @param errorCode DOCUMENT ME!
     * @param timeStamp DOCUMENT ME!
     */
    public ErrorListEntry(int errorCode, Date timeStamp) {
        this.errorCode = errorCode;
        this.tinestamp = timeStamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the tinestamp
     */
    public Date getTinestamp() {
        return tinestamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tinestamp the tinestamp to set
     */
    public void setTinestamp(Date tinestamp) {
        this.tinestamp = tinestamp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        return String.format("%02x  %s", errorCode & 0xFF, tinestamp.toString());
    }
}
