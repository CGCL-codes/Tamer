package net.alteiar.shared.xml;

import java.awt.Color;
import org.jdom.Element;

/**
 * 
 * @author Cody Stoutenburg
 */
public class XmlUtils {

    public static Color ColorfromXml(final Element e) {
        int r = Integer.valueOf(e.getAttributeValue("r"));
        int g = Integer.valueOf(e.getAttributeValue("g"));
        int b = Integer.valueOf(e.getAttributeValue("b"));
        return new Color(r, g, b);
    }
}
