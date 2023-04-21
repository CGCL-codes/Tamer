package org.jcvi.glk.elvira.flu.report.importer;

public class ImportException extends Exception {

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
