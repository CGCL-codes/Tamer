package gnu.xml.dom.html2;

import org.w3c.dom.html2.HTMLTableColElement;

/**
 * An HTML 'COL' or 'COLGROUP' element node.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class DomHTMLTableColElement extends DomHTMLElement implements HTMLTableColElement {

    protected DomHTMLTableColElement(DomHTMLDocument owner, String namespaceURI, String name) {
        super(owner, namespaceURI, name);
    }

    public String getAlign() {
        return getHTMLAttribute("align");
    }

    public void setAlign(String align) {
        setHTMLAttribute("align", align);
    }

    public String getCh() {
        return getHTMLAttribute("char");
    }

    public void setCh(String ch) {
        setHTMLAttribute("char", ch);
    }

    public String getChOff() {
        return getHTMLAttribute("charoff");
    }

    public void setChOff(String chOff) {
        setHTMLAttribute("charoff", chOff);
    }

    public int getSpan() {
        return getIntHTMLAttribute("span");
    }

    public void setSpan(int span) {
        setIntHTMLAttribute("span", span);
    }

    public String getVAlign() {
        return getHTMLAttribute("valign");
    }

    public void setVAlign(String vAlign) {
        setHTMLAttribute("valign", vAlign);
    }

    public String getWidth() {
        return getHTMLAttribute("width");
    }

    public void setWidth(String width) {
        setHTMLAttribute("width", width);
    }
}
