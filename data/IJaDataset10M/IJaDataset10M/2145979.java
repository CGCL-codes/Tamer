package cookxml.core.interfaces;

import org.w3c.dom.Element;
import cookxml.core.DecodeEngine;

/**
 * This interface is used for the rare occation when one wants to create an
 * object not based on the element tag name, but other characteristics.
 *
 * @author Heng Yuan
 * @version $Id: SpecialCreator.java 218 2007-06-06 06:10:10Z coconut $
 * @since CookXml 3.0
 */
public interface SpecialCreator {

    public Creator getCreator(DecodeEngine decodeEngine, String parentNS, String parentTag, Element elm, Object parentObj);
}
