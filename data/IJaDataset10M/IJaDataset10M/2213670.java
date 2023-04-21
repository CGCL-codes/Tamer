package org.apache.batik.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class represents uri values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: URIValue.java,v 1.1 2005/11/21 09:51:39 dev Exp $
 */
public class URIValue extends StringValue {

    String cssText;

    /**
     * Creates a new StringValue.
     */
    public URIValue(String cssText, String uri) {
        super(CSSPrimitiveValue.CSS_URI, uri);
        this.cssText = cssText;
    }

    /**
     * A string representation of the current value. 
     */
    public String getCssText() {
        return "url(" + cssText + ")";
    }
}
