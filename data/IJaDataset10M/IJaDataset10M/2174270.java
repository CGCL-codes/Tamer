package org.torweg.pulse.component.core.site;

import org.jdom.Element;
import org.torweg.pulse.bundle.Result;

/**
 * the result of the {@code StyleController}.
 * 
 * @author Daniel Dietz
 * @version $Revision: 1941 $
 */
@Deprecated
public class StyleControllerResult implements Result {

    /**
	 * the styles of the result.
	 */
    private Element styles;

    /**
	 * returns the result of the {@code StyleController}.
	 * 
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 * 
	 * @return the result of the {@code StyleController}
	 */
    public Element deserializeToJDOM() {
        Element result = new Element("result").setAttribute("class", this.getClass().getCanonicalName());
        if (this.styles != null) {
            result.addContent(this.styles.detach());
        }
        return result;
    }

    /**
	 * set the styles of the result.
	 * 
	 * @param s
	 *            the styles to set
	 */
    public final void setStyles(final Element s) {
        this.styles = s;
    }
}
