package org.xmlcml.jumbo3;

import org.w3c.dom.Document;
import org.xmlcml.cml.CMLDocument;
import org.xmlcml.cml.CMLDocumentFactory;

/** manufacture CMLBonds of an appropriate subclass

@author (C) P. Murray-Rust, 2000
*/
public class DocumentFactoryImpl implements CMLDocumentFactory {

    public DocumentFactoryImpl() {
    }

    /** default is to create a CMLDocument */
    public CMLDocument createDocument() {
        return new CMLDocumentImpl();
    }
}
