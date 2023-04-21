package org.ozoneDB.xml.dom.html;

import org.ozoneDB.xml.dom.ElementImpl;
import org.w3c.dom.html.HTMLOListElement;

/**
 * @version $Revision: 1.1 $ $Date: 2003/11/22 19:39:11 $
 * @author <a href="mailto:arkin@trendline.co.il">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLOListElement
 * @see ElementImpl
 */
public final class HTMLOListElementImpl extends HTMLElementImpl implements HTMLOListElement {

    public boolean getCompact() {
        return getAttribute("compact") != null;
    }

    public void setCompact(boolean compact) {
        setAttribute("compact", compact ? "" : null);
    }

    public int getStart() {
        return toInteger(getAttribute("start"));
    }

    public void setStart(int start) {
        setAttribute("start", String.valueOf(start));
    }

    public String getType() {
        return getAttribute("type");
    }

    public void setType(String type) {
        setAttribute("type", type);
    }

    /**
     * Constructor requires owner document.
     *
     * @param owner The owner HTML document
     */
    public HTMLOListElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, "OL");
    }
}
