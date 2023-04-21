package com.javabi.htmlbuilder.html.element.header;

import com.javabi.htmlbuilder.html.HTMLElementList;
import com.javabi.htmlbuilder.html.HTMLElementName;

/**
 * An HTML Header 3 Element.
 * <p>
 * &lt;h3&gt; ... &lt;/h3&gt;
 * </p>
 */
public class Header3 extends HTMLElementList<Header3> {

    /**
	 * Returns the element name.
	 * @return the element name.
	 */
    public HTMLElementName getElementName() {
        return HTMLElementName.HEADER_3;
    }
}
