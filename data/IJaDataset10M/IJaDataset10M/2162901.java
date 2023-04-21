package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

/**
 * Wrapper class for PdfCopy and PdfSmartCopy.
 * Allows you to concatenate existing PDF documents with much less code.
 */
public class PdfConcatenate {

    /** The Document object for PdfCopy. */
    protected Document document;

    /** The actual PdfWriter */
    protected PdfCopy copy;

    /**
	 * Creates an instance of the concatenation class.
	 * @param os	the OutputStream for the PDF document
	 */
    public PdfConcatenate(OutputStream os) throws DocumentException {
        this(os, false);
    }

    /**
	 * Creates an instance of the concatenation class.
	 * @param os	the OutputStream for the PDF document
	 * @param smart	do we want PdfCopy to detect redundant content?
	 */
    public PdfConcatenate(OutputStream os, boolean smart) throws DocumentException {
        document = new Document();
        if (smart) copy = new PdfSmartCopy(document, os); else copy = new PdfCopy(document, os);
    }

    /**
	 * Adds the pages from an existing PDF document.
	 * @param reader	the reader for the existing PDF document
	 * @return			the number of pages that were added
	 * @throws DocumentException
	 * @throws IOException
	 */
    public int addPages(PdfReader reader) throws DocumentException, IOException {
        open();
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            copy.addPage(copy.getImportedPage(reader, i));
        }
        copy.freeReader(reader);
        return n;
    }

    /**
	 * Gets the PdfCopy instance so that you can add bookmarks or change preferences before you close PdfConcatenate.
	 */
    public PdfCopy getWriter() {
        return copy;
    }

    /**
	 * Opens the document (if it isn't open already).
	 * Opening the document is done implicitly.
	 */
    public void open() {
        if (!document.isOpen()) {
            document.open();
        }
    }

    /**
	 * We've finished writing the concatenated document.
	 */
    public void close() {
        document.close();
    }
}
