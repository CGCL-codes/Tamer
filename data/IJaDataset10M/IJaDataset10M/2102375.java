package org.allcolor.html.parser;

import org.allcolor.xml.parser.dom.ADocument;
import org.allcolor.xml.parser.dom.ANode;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLTableRowElement;
import java.util.Arrays;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author Quentin Anciaux
 */
public class CHTMLTdElement extends CHTMLElement implements HTMLTableCellElement {

    static final long serialVersionUID = -1940399152276045762L;

    private static final List ve = Arrays.asList(new String[] { "#PCDATA", "p", "h1", "h2", "h3", "h4", "h5", "h6", "div", "ul", "ol", "dl", "menu", "dir", "pre", "hr", "blockquote", "address", "center", "noframes", "isindex", "fieldset", "table", "form", "a", "br", "span", "bdo", "object", "applet", "img", "map", "iframe", "tt", "i", "b", "u", "s", "strike", "big", "small", "font", "basefont", "em", "strong", "dfn", "code", "q", "samp", "kbd", "var", "cite", "abbr", "acronym", "sub", "sup", "input", "select", "textarea", "label", "button", "noscript", "ins", "del", "script" });

    /**
     * DOCUMENT ME!
     *
     * @param ownerDocument
     */
    public CHTMLTdElement(ADocument ownerDocument) {
        super("td", ownerDocument);
        validElement = ve;
    }

    public String getDefaultParentType() {
        return null;
    }

    public int getCellIndex() {
        ANode parent = null;
        parent = parentNode;
        while (parent != null && parent.name != "tr") {
            parent = parent.parentNode;
        }
        if (parent == null) return -1;
        HTMLCollection nl = ((HTMLTableRowElement) parent).getCells();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n == this) return i;
        }
        return -1;
    }

    public String getAbbr() {
        return getAttribute("abbr");
    }

    public void setAbbr(String abbr) {
        setAttribute("abbr", abbr);
    }

    public String getAlign() {
        return getAttribute("align");
    }

    public void setAlign(String align) {
        setAttribute("align", align);
    }

    public String getAxis() {
        return getAttribute("axis");
    }

    public void setAxis(String axis) {
        setAttribute("axis", axis);
    }

    public String getBgColor() {
        return getAttribute("bgcolor");
    }

    public void setBgColor(String bgColor) {
        setAttribute("bgcolor", bgColor);
    }

    public String getCh() {
        return getAttribute("char");
    }

    public void setCh(String ch) {
        setAttribute("char", ch);
    }

    public String getChOff() {
        return getAttribute("charoff");
    }

    public void setChOff(String chOff) {
        setAttribute("charoff", chOff);
    }

    public int getColSpan() {
        try {
            return Integer.parseInt(getAttribute("colspan"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setColSpan(int colSpan) {
        setAttribute("colspan", "" + colSpan);
    }

    public String getHeaders() {
        return getAttribute("headers");
    }

    public void setHeaders(String headers) {
        setAttribute("headers", headers);
    }

    public String getHeight() {
        return getAttribute("height");
    }

    public void setHeight(String height) {
        setAttribute("height", height);
    }

    public boolean getNoWrap() {
        return "nowrap".equals(getAttribute("nowrap"));
    }

    public void setNoWrap(boolean noWrap) {
        if (noWrap) {
            setAttribute("nowrap", "nowrap");
        } else {
            removeAttribute("nowrap");
        }
    }

    public int getRowSpan() {
        try {
            return Integer.parseInt(getAttribute("rowspan"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setRowSpan(int rowSpan) {
        setAttribute("rowspan", "" + rowSpan);
    }

    public String getScope() {
        return getAttribute("scope");
    }

    public void setScope(String scope) {
        setAttribute("scope", scope);
    }

    public String getVAlign() {
        return getAttribute("valign");
    }

    public void setVAlign(String vAlign) {
        setAttribute("valign", vAlign);
    }

    public String getWidth() {
        return getAttribute("width");
    }

    public void setWidth(String width) {
        setAttribute("width", width);
    }
}
