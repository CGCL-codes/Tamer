package org.primordion.xholon.service.mathscieng;

import org.primordion.xholon.base.Xholon;
import org.primordion.xholon.io.xml.IXmlReader;

/**
 * This class knows how to find the name of a concrete Quantity class.
 * It returns an implName based on the number of dimensions specified
 * in the XML text of the XML Quantity element.
 * <p>ex: "&lt;Displacement>123.4 m&lt;/Displacement>"
 * returns "org.primordion.xholon.service.mathscieng.Quantity"</p>
 * <p>ex: "&lt;Displacement>11.1 22.2 m&lt;/Displacement>"
 * returns "org.primordion.xholon.service.mathscieng.QuantityVector"</p>
 * <p>TODO Calling xmlReader.next() prevents Quantity from having any attributes such as roleName.</p>
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on December 26, 2011)
 * @deprecated
 */
public abstract class QuantityFactory extends Xholon {

    /**
	 * Return the implementation name of a concrete class.
	 * This is the method expected by Xml2Xholon.
	 * @param obj Must be an instance of IXmlReader.
	 * @return The implName of a concrete class.
	 */
    public static String implName(Object obj) {
        IXmlReader xmlReader = (IXmlReader) obj;
        xmlReader.next();
        String textVal = xmlReader.getText().trim();
        if ((textVal != null) && (textVal.length() > 0)) {
            if (textVal.split(" ").length > 2) {
                return "org.primordion.xholon.service.mathscieng.QuantityVector";
            }
        }
        return "org.primordion.xholon.service.mathscieng.Quantity";
    }
}
