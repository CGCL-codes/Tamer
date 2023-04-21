package com.cateshop.exe;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * @author notXX
 */
public class XmlChunk extends Chunk {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return the document
     * @throws DocumentException
     */
    public Document getDocument() throws DocumentException {
        return DocumentHelper.parseText(getValue());
    }

    /**
     * @param document
     *            the document to set
     */
    public void setDocument(Document document) {
        setValue(document.asXML());
    }
}
