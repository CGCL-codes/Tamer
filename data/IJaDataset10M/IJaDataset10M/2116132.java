package org.omegat.filters3.xml;

import org.omegat.filters3.Attribute;
import org.omegat.filters3.Attributes;
import org.omegat.util.StaticUtils;

/**
 * Static XML utility methods.
 *
 * @author Maxym Mykhalchuk
 */
public final class XMLUtils {

    /** Private to disallow creation. */
    private XMLUtils() {
    }

    /** Converts attributes from org.xml.sax package to OmegaT's. */
    public static Attributes convertAttributes(org.xml.sax.Attributes attributes) {
        Attributes res = new Attributes();
        if (attributes == null) return res;
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = StaticUtils.makeValidXML(attributes.getQName(i));
            String value = StaticUtils.makeValidXML(attributes.getValue(i));
            Attribute attr = new Attribute(name, value);
            res.add(attr);
        }
        return res;
    }
}
