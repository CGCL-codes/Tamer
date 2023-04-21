package org.keyintegrity.webbeans.metadata.store;

public class MetadataNotFoundException extends MetadataStoreException {

    /**
     * @since 1.0-SNAPSHOT
     */
    private static final long serialVersionUID = 6776275211117752861L;

    public MetadataNotFoundException() {
    }

    public MetadataNotFoundException(String message) {
        super(message);
    }

    public MetadataNotFoundException(Throwable cause) {
        super(cause);
    }

    public MetadataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
