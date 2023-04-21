package org.jxul.css;

import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.ViewCSS;
import org.w3c.dom.views.DocumentView;

/**
 * @author Will Etson
 */
public class CSSView implements ViewCSS {

    /**
	 * @see org.w3c.dom.css.ViewCSS#getComputedStyle(org.w3c.dom.Element, java.lang.String)
	 */
    public CSSStyleDeclaration getComputedStyle(Element elt, String pseudoElt) {
        return null;
    }

    public DocumentView getDocument() {
        return null;
    }
}
