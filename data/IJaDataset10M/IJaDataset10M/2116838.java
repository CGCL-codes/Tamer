package ag.ion.bion.officelayer.internal.text;

import ag.ion.bion.officelayer.text.IPageStyle;
import ag.ion.bion.officelayer.text.IPageStyleProperties;
import com.sun.star.beans.XPropertySet;
import com.sun.star.style.XStyle;
import com.sun.star.uno.UnoRuntime;

/**
 * Style of page of a text document.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 10398 $
 */
public class PageStyle implements IPageStyle {

    private XStyle xStyle = null;

    /**
	 * Constructs new PageStyle.
	 * 
	 * @param xStyle
	 *            OpenOffice.org XStyle interface
	 * 
	 * @throws IllegalArgumentException
	 *             if the submitted OpenOffice.org XStyle interface is not valid
	 * 
	 * @author Andreas Br�ker
	 */
    public PageStyle(XStyle xStyle) throws IllegalArgumentException {
        if (xStyle == null) throw new IllegalArgumentException("Submitted OpenOffice.org XStyle interface is not valid.");
        this.xStyle = xStyle;
    }

    /**
	 * Returns name of the page style.
	 * 
	 * @return name of the page style
	 * 
	 * @author Andreas Br�ker
	 */
    public String getName() {
        return xStyle.getName();
    }

    /**
	 * Returns page style properties.
	 * 
	 * @return page style properties
	 * 
	 * @author Andreas Br�ker
	 */
    public IPageStyleProperties getProperties() {
        XPropertySet xPropertySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xStyle);
        return new PageStyleProperties(xPropertySet);
    }
}
