package moller.util.xml;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLUtil {

    /**
	 * @param encoding
	 * @param version
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeStartDocument(String encoding, String version, XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeStartDocument(encoding, version);
    }

    /**
	 * @param element
	 * @param value
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeElement(String element, String value, XMLStreamWriter xmlsw) throws XMLStreamException {
        writeElement(element, value, null, xmlsw);
    }

    /**
	 * @param element
	 * @param value
	 * @param xmlAttributes
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeElement(String element, String value, XMLAttribute[] xmlAttributes, XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeStartElement(element);
        if (value != null) {
            xmlsw.writeCharacters(value);
        }
        if (xmlAttributes != null) {
            for (XMLAttribute xmlAttribute : xmlAttributes) {
                xmlsw.writeAttribute(xmlAttribute.getName(), xmlAttribute.getValue());
            }
        }
        xmlsw.writeEndElement();
    }

    /**
	 * @param element
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeElementStart(String element, XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeStartElement(element);
    }

    /**
	 * @param element
	 * @param attributeName
	 * @param attributeValue
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeElementStart(String element, String attributeName, String attributeValue, XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeStartElement(element);
        xmlsw.writeAttribute(attributeName, attributeValue);
    }

    /**
	 * @param element
	 * @param xmlAttributes
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeElementStart(String element, XMLAttribute[] xmlAttributes, XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeStartElement(element);
        for (XMLAttribute xmlAttribute : xmlAttributes) {
            xmlsw.writeAttribute(xmlAttribute.getName(), xmlAttribute.getValue());
        }
    }

    /**
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeElementEnd(XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeEndElement();
    }

    /**
	 * @param comment
	 * @param xmlsw
	 * @throws XMLStreamException
	 */
    public static void writeComment(String comment, XMLStreamWriter xmlsw) throws XMLStreamException {
        xmlsw.writeComment(comment);
    }
}
