package eu.pisolutions.ocelot.document.io.error;

import eu.pisolutions.ocelot.document.io.DocumentReadingException;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.object.PdfObjectIdentifier;

public interface DocumentReadingErrorHandler {

    void handleReadingError(PdfObjectIdentifier identifier, PdfObject pdfObject, DocumentReadingException exception);
}
