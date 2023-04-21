package gps.garmin.img.structure.data;

import java.util.Date;

/**
 *
 * @author aranzuglia
 */
public class SubfileCommonHeader {

    private int headersLength;

    private SubfileType type;

    private SubfileLockStatus lockStatus;

    private Date creationDate;

    /**
     * Creates a new instance of SubfileCommonHeader
     */
    public SubfileCommonHeader() {
    }

    public int getHeadersLength() {
        return headersLength;
    }

    public void setHeadersLength(int headersLength) {
        this.headersLength = headersLength;
    }

    public SubfileType getType() {
        return type;
    }

    public void setType(SubfileType type) {
        this.type = type;
    }

    public SubfileLockStatus getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(SubfileLockStatus lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
