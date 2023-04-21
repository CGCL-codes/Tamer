package org.gvsig.gpe.kml.utils;

import java.util.Hashtable;
import javax.xml.namespace.QName;
import org.gvsig.gpe.GPEDefaults;
import org.gvsig.gpe.utils.StringUtils;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;

/**
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 */
public class KMLUtilsParser {

    /**
	 * It returns a HashTable with the XML attributes. It has been 
	 * created because the parser doesn't has a getAttribiute(AttributeName)
	 * method.

	 * @param parser
	 * @return
	 * @throws XmlStreamException 
	 */
    public static Hashtable getAttributes(IXmlStreamReader parser) throws XmlStreamException {
        Hashtable hash = new Hashtable();
        int num_atributos = parser.getAttributeCount();
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            QName atributo = parser.getAttributeName(i);
            String valor = parser.getAttributeValue(i);
            if (valor != null) hash.put(atributo, valor);
        }
        return hash;
    }

    /**
	 * Remove the blanc symbol from a tag
	 * @param tag
	 * Tag name
	 * @return
	 * The tag without blancs
	 */
    public static String removeBlancSymbol(QName tag) {
        if (tag == null) {
            return null;
        }
        String blancSpace = GPEDefaults.getStringProperty(XmlProperties.DEFAULT_BLANC_SPACE);
        if (blancSpace == null) {
            blancSpace = Kml2_1_Tags.DEFAULT_BLANC_SPACE;
        }
        return StringUtils.replaceAllString(tag.getLocalPart(), blancSpace, " ");
    }

    /**
	 * Replace the blancs of a tag with the
	 * deafult blanc symbol
	 * @param name
	 * @return
	 * A tag with blancs
	 */
    public static String addBlancSymbol(QName name) {
        if (name == null) {
            return null;
        }
        String blancSpace = GPEDefaults.getStringProperty(XmlProperties.DEFAULT_BLANC_SPACE);
        if (blancSpace == null) {
            blancSpace = Kml2_1_Tags.DEFAULT_BLANC_SPACE;
        }
        return StringUtils.replaceAllString(name.getLocalPart(), " ", blancSpace);
    }
}
