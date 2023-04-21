package org.in4ama.documentengine.converter.odt;

import java.io.InputStream;
import org.in4ama.documentengine.output.PdfOutputDocument;

/** Converts ODT documents to PDF documents */
public class Odt2PdfConverter extends OdtConverter {

    public static final String OUTPUT_TYPE = "writer_pdf_Export";

    /** Builds and returns a PDF output document out of the specified input stream */
    @Override
    protected PdfOutputDocument createOutputDocument(InputStream inputStream) {
        return new PdfOutputDocument(inputStream);
    }

    /** Returns the output type of this converter, i.e. PDF document */
    @Override
    protected String getOutputType() {
        return OUTPUT_TYPE;
    }
}
