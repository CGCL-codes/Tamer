package org.ozoneDB.xml.dom.html;

import org.ozoneDB.xml.dom.ElementImpl;
import org.w3c.dom.html.HTMLBaseElement;

/**
 * @version $Revision: 1.1 $ $Date: 2003/11/22 19:39:11 $
 * @author <a href="mailto:arkin@trendline.co.il">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLBaseElement
 * @see ElementImpl
 */
public final class HTMLBaseElementImpl extends HTMLElementImpl implements HTMLBaseElement {

    public String getHref() {
        return getAttribute("href");
    }

    public void setHref(String href) {
        setAttribute("href", href);
    }

    public String getTarget() {
        return getAttribute("target");
    }

    public void setTarget(String target) {
        setAttribute("target", target);
    }

    /**
     * Constructor requires owner document.
     *
     * @param owner The owner HTML document
     */
    public HTMLBaseElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, "BASE");
    }
}
