package gnu.xml.dom.html2;

import org.w3c.dom.html2.HTMLDivElement;

/**
 * An HTML 'DIV' element node.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class DomHTMLDivElement extends DomHTMLElement implements HTMLDivElement {

    protected DomHTMLDivElement(DomHTMLDocument owner, String namespaceURI, String name) {
        super(owner, namespaceURI, name);
    }

    public String getAlign() {
        return getHTMLAttribute("align");
    }

    public void setAlign(String align) {
        setHTMLAttribute("align", align);
    }
}
