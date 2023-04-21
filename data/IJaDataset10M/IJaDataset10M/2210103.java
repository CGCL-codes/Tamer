package org.allcolor.html.parser;

import org.allcolor.xml.parser.dom.ADocument;
import java.util.Arrays;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLBdoElement extends CHTMLElement {

    static final long serialVersionUID = -3632401269635161134L;

    private static final List ve = Arrays.asList(new String[] { "#PCDATA", "a", "br", "span", "bdo", "object", "applet", "img", "map", "iframe", "tt", "i", "b", "u", "s", "strike", "big", "small", "font", "basefont", "em", "strong", "dfn", "code", "q", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "input", "select", "textarea", "label", "button", "ins", "del", "script" });

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLBdoElement(ADocument ownerDocument) {
        super("bdo", ownerDocument);
        validElement = ve;
    }

    public String getDefaultParentType() {
        return null;
    }
}
