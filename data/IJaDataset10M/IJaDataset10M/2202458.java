package org.objectwiz.core.facet.customization;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

/**
 * A resource with binary content (image, document, etc.).
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class BinaryResource extends ApplicationSpecificObject {

    private String name;

    private String originalUrl;

    private String mimeType;

    private byte[] bytes;

    /** Public no-args constructor */
    public BinaryResource() {
    }

    public BinaryResource(Application application, String name, String originalUrl, String mimeType, byte[] data) {
        this(name, originalUrl, mimeType, data);
        this.setApplication(application);
    }

    public BinaryResource(String name, String originalUrl, String mimeType, byte[] data) {
        this.name = name;
        this.originalUrl = originalUrl;
        this.mimeType = mimeType;
        this.bytes = data;
    }

    /**
     * Human-readable name of this resource.
     */
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Mime-type.
     * @return
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Original url (if any) where this resource was fetched from.
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * Raw data.
     */
    @NotNull
    @Lob
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "[" + getClass().getSimpleName() + "] " + getId() + ": " + name + " (Mime-type: " + mimeType + ") - " + (bytes == null ? "(no data)" : bytes.length + " bytes");
    }

    public BinaryResource clone() {
        BinaryResource copy = new BinaryResource(name, originalUrl, mimeType, getBytes());
        copy.setId(getId());
        return copy;
    }
}
