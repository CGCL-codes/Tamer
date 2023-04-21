package org.torweg.pulse.site.content.linkcorrection;

import org.jdom.Element;

/**
 * One size fits all interface to return HTML for link correction.
 * 
 * @author Thomas Weber
 * @version $Revision: 2375 $
 */
public interface ILinkCorrectableElement {

    /**
	 * returns the {@code Element} for link correcting.
	 * 
	 * @return the element for link correcting
	 */
    Element getElement();

    /**
	 * sets the corrected {@code Element}.
	 * 
	 * @param e
	 *            the processed {@code Element}
	 */
    void setElement(Element e);
}
