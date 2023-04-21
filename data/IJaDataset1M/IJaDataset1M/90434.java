package org.allcolor.html2.parser;

import org.allcolor.xml.parser.dom.ADocument;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLStrikeElement extends CHTMLElement {

    static final long serialVersionUID = -3444423585140384131L;

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLStrikeElement(ADocument ownerDocument) {
        super("strike", ownerDocument);
    }

    public String getDefaultParentType() {
        return null;
    }
}
