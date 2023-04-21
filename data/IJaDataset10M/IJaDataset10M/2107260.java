package net.deytan.tagger.exception;

public class MetaDataServiceException extends Exception {

    public MetaDataServiceException(final String message) {
        super(message);
    }

    public MetaDataServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
