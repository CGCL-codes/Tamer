package org.redwood.business.report.reportgeneration.reportfactory;

/**
 *
 * @author  Gerrit Franke
 * @version 1.0 
 */
public class DocumentStoreException extends java.lang.Exception {

    /**
     * Creates new <code>ReportGenerationFailedException</code> without detail message.
     */
    public DocumentStoreException() {
    }

    /**
     * Constructs an <code>ReportGenerationFailedException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DocumentStoreException(String msg) {
        super(msg);
    }
}
