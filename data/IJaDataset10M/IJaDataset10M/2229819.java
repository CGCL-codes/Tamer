package org.ozoneDB.xml.dom.html;

import org.ozoneDB.xml.dom.ElementImpl;
import org.w3c.dom.html.HTMLFieldSetElement;

/**
 * @version $Revision: 1.1 $ $Date: 2003/11/22 19:39:11 $
 * @author <a href="mailto:arkin@trendline.co.il">Assaf Arkin</a>
 * @see org.w3c.dom.html.HTMLFieldSetElement
 * @see ElementImpl
 */
public final class HTMLFieldSetElementImpl extends HTMLElementImpl implements HTMLFieldSetElement, HTMLFormControl {

    /**
     * Constructor requires owner document.
     *
     * @param owner The owner HTML document
     */
    public HTMLFieldSetElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, "FIELDSET");
    }
}
