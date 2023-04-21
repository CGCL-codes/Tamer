package no.tstsolutions.tvedere.domain.persistent;

import javax.persistence.Entity;

/**
 * Abstract class with common properties for data objects to 
 * be persisted.
 * @author asm
 */
@Entity
public abstract class DataObject extends PersistentObject {

    private byte[] data;

    /**
     * Returns the MIME type for data object
     * @return String with mime type
     */
    public abstract String getMIMEType();

    /**
     * Returns the binary data for this data object
     * @return
     */
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
