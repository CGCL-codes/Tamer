package org.allcolor.html.parser;

import org.allcolor.xml.parser.dom.ADocument;
import org.w3c.dom.html.HTMLModElement;
import java.util.Arrays;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLDelElement extends CHTMLElement implements HTMLModElement {

    static final long serialVersionUID = -2017656490490261065L;

    private static final List ve = Arrays.asList(new String[] { "#PCDATA", "p", "h1", "h2", "h3", "h4", "h5", "h6", "div", "ul", "ol", "dl", "menu", "dir", "pre", "hr", "blockquote", "address", "center", "noframes", "isindex", "fieldset", "table", "form", "a", "br", "span", "bdo", "object", "applet", "img", "map", "iframe", "tt", "i", "b", "u", "s", "strike", "big", "small", "font", "basefont", "em", "strong", "dfn", "code", "q", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "input", "select", "textarea", "label", "button", "noscript", "ins", "del", "script" });

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLDelElement(ADocument ownerDocument) {
        super("del", ownerDocument);
        validElement = ve;
    }

    public String getDefaultParentType() {
        return null;
    }

    public String getCite() {
        return getAttribute("cite");
    }

    public String getDateTime() {
        return getAttribute("datetime");
    }

    public void setCite(String cite) {
        setAttribute("cite", cite);
    }

    public void setDateTime(String dateTime) {
        setAttribute("datetime", dateTime);
    }
}
