package org.allcolor.html2.parser;

import org.allcolor.xml.parser.dom.ADocument;
import java.util.Arrays;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLStrongElement extends CHTMLElement {

    static final long serialVersionUID = -4360812826721115068L;

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLStrongElement(ADocument ownerDocument) {
        super("strong", ownerDocument);
        validElement = Arrays.asList(new String[] { "#PCDATA", "a", "br", "span", "bdo", "object", "applet", "img", "map", "iframe", "tt", "i", "b", "u", "s", "strike", "big", "small", "font", "basefont", "em", "strong", "dfn", "code", "q", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "input", "select", "textarea", "label", "button", "ins", "del", "script" });
    }

    public String getDefaultParentType() {
        return null;
    }
}
