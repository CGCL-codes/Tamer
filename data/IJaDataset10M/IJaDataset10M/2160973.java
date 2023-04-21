package cx.ath.contribs.internal.html.dom;

import org.w3c.dom.html.HTMLIsIndexElement;

/**
 * @xerces.internal
 * @version $Revision: 1.1 $ $Date: 2007/06/02 09:58:58 $
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLIsIndexElement
 * @see cx.ath.contribs.internal.xerces.dom.ElementImpl
 */
public class HTMLIsIndexElementImpl extends HTMLElementImpl implements HTMLIsIndexElement {

    private static final long serialVersionUID = 3073521742049689699L;

    public String getPrompt() {
        return getAttribute("prompt");
    }

    public void setPrompt(String prompt) {
        setAttribute("prompt", prompt);
    }

    /**
     * Constructor requires owner document.
     * 
     * @param owner The owner HTML document
     */
    public HTMLIsIndexElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }
}
