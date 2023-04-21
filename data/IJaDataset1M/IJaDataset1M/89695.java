package org.infoset.xml.dom;

import org.infoset.xml.Characters;

/**
 *
 * @author alex
 */
public class DOMCDataSectionProxy extends DOMCharactersProxy implements org.w3c.dom.CDATASection {

    /** Creates a new instance of DOMCDataSectionProxy */
    public DOMCDataSectionProxy(Characters cdata, DOMDocumentProxy docProxy) {
        super(cdata, docProxy);
    }
}
