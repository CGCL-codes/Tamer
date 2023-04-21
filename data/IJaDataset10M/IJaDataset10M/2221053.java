package org.xpresso.xml;

import java.io.Serializable;

/**
 * The class XmlContent describes common features needed for all type of XML content. 
 * This code is under the <a href="http://www.gnu.org/licenses/lgpl.html">LGPL v3 licence</a>.
 * @author Alexis Dufrenoy
 *
 */
public abstract class XmlContent implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6581211672106559056L;

    /**
	 * The resulting tag after construction
	 */
    protected StringBuilder content;

    /**
	 * Return the tag as a String
	 * @return String containing the XmlContent
	 */
    public String getText() {
        return content.toString();
    }
}
