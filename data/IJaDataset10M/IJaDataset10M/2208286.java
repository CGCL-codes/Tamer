package com.artofsolving.jodconverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicDocumentFormatRegistry implements DocumentFormatRegistry {

    private List documentFormats = new ArrayList();

    public void addDocumentFormat(DocumentFormat documentFormat) {
        documentFormats.add(documentFormat);
    }

    protected List getDocumentFormats() {
        return documentFormats;
    }

    /**
	 * @param extension the file extension
	 * @return the DocumentFormat for this extension, or null if the extension is not mapped
	 */
    public DocumentFormat getFormatByFileExtension(String extension) {
        if (extension == null) {
            return null;
        }
        String lowerExtension = extension.toLowerCase();
        for (Iterator it = documentFormats.iterator(); it.hasNext(); ) {
            DocumentFormat format = (DocumentFormat) it.next();
            if (format.getFileExtension().equals(lowerExtension)) {
                return format;
            }
        }
        return null;
    }

    public DocumentFormat getFormatByMimeType(String mimeType) {
        for (Iterator it = documentFormats.iterator(); it.hasNext(); ) {
            DocumentFormat format = (DocumentFormat) it.next();
            if (format.getMimeType().equals(mimeType)) {
                return format;
            }
        }
        return null;
    }
}
