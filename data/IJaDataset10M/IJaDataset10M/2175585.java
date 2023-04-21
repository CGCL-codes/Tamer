package gnu.xml.dom.html2;

import org.w3c.dom.html2.HTMLHeadElement;

/**
 * An HTML 'HEAD' element node.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
public class DomHTMLHeadElement extends DomHTMLElement implements HTMLHeadElement {

    protected DomHTMLHeadElement(DomHTMLDocument owner, String namespaceURI, String name) {
        super(owner, namespaceURI, name);
    }

    public String getProfile() {
        return getHTMLAttribute("profile");
    }

    public void setProfile(String profile) {
        setHTMLAttribute("profile", profile);
    }
}
