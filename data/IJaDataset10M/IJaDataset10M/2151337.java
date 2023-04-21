package cx.ath.contribs.internal.html.dom;

import org.w3c.dom.html.HTMLTableCaptionElement;

/**
 * @xerces.internal
 * @version $Revision: 1.1 $ $Date: 2007/06/02 09:58:58 $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLTableCaptionElement
 * @see cx.ath.contribs.internal.xerces.dom.ElementImpl
 */
public class HTMLTableCaptionElementImpl extends HTMLElementImpl implements HTMLTableCaptionElement {

    private static final long serialVersionUID = 183703024771848940L;

    public String getAlign() {
        return getAttribute("align");
    }

    public void setAlign(String align) {
        setAttribute("align", align);
    }

    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLTableCaptionElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }
}
