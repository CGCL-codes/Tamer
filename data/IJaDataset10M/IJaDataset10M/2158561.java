package org.allcolor.html2.parser;

import java.util.Arrays;
import java.util.List;
import org.allcolor.xml.parser.dom.ADocument;
import org.w3c.dom.html2.HTMLTableCaptionElement;
import org.w3c.dom.html2.HTMLTableSectionElement;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLCaptionElement extends CHTMLTableElement implements HTMLTableCaptionElement, HTMLTableSectionElement {

    static final long serialVersionUID = 8628460906355889028L;

    private static final List ve = Arrays.asList(new String[] { "#PCDATA", "a", "br", "span", "bdo", "object", "applet", "img", "map", "iframe", "tt", "i", "b", "u", "s", "strike", "big", "small", "font", "basefont", "em", "strong", "dfn", "code", "q", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "input", "select", "textarea", "label", "button", "ins", "del", "script" });

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLCaptionElement(ADocument ownerDocument) {
        super("caption", ownerDocument);
        validElement = ve;
    }
}
