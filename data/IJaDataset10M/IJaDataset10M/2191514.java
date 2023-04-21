package org.apache.html.dom;

import org.w3c.dom.html.HTMLStyleElement;

/**
 * @xerces.internal
 * @version $Revision: 447255 $ $Date: 2006-09-18 01:36:42 -0400 (Mon, 18 Sep 2006) $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLStyleElement
 * @see org.apache.xerces.dom.ElementImpl
 */
public class HTMLStyleElementImpl extends HTMLElementImpl implements HTMLStyleElement {

    private static final long serialVersionUID = -9001815754196124532L;

    public boolean getDisabled() {
        return getBinary("disabled");
    }

    public void setDisabled(boolean disabled) {
        setAttribute("disabled", disabled);
    }

    public String getMedia() {
        return getAttribute("media");
    }

    public void setMedia(String media) {
        setAttribute("media", media);
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
    public HTMLStyleElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }
}
