package org.allcolor.html.parser;

import org.allcolor.xml.parser.dom.ADocument;
import java.util.Arrays;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLSmallElement extends CHTMLElement {

    static final long serialVersionUID = 2886437061646840946L;

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLSmallElement(ADocument ownerDocument) {
        super("small", ownerDocument);
        validElement = Arrays.asList(new String[] { "#PCDATA", "a", "br", "span", "bdo", "object", "applet", "img", "map", "iframe", "tt", "i", "b", "u", "s", "strike", "big", "small", "font", "basefont", "em", "strong", "dfn", "code", "q", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "input", "select", "textarea", "label", "button", "ins", "del", "script" });
    }

    public String getDefaultParentType() {
        return null;
    }
}
