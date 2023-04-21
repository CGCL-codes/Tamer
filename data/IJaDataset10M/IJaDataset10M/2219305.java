package org.apache.axis.message.addressing.util;

import javax.xml.soap.SOAPElement;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Class TextExtractor.
 *
 * @author Davanum Srinivas
 * @version $Revision: 14 $
 */
public abstract class TextExtractor {

    /**
   * Hidden constructor.
   */
    private TextExtractor() {
    }

    /**
   * Gets a QName from the specified value, looking up the prefix in
   * the specified SOAP element.
   *
   * @param value Value
   * @param elem  Element for namespace look up
   * @return QName instance
   */
    public static QName getQName(String value, SOAPElement elem) {
        int p = value.indexOf(':');
        String prefix = (p == -1) ? "" : value.substring(0, p);
        String ns = elem.getNamespaceURI(prefix);
        return new QName(ns, value.substring(p + 1), prefix);
    }

    /**
   * Gets the text in the specified XML node.
   *
   * @param node Node to parse
   * @return Text
   */
    public static String getText(Node node) {
        if (node == null) {
            return "";
        }
        if (node instanceof Text) {
            return node.getNodeValue().trim();
        }
        StringBuffer result = new StringBuffer();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node child = list.item(i);
            if (child instanceof Text) {
                result.append(child.getNodeValue());
            } else {
                break;
            }
        }
        return result.toString().trim();
    }

    /**
   * Gets the text contained in the specified SOAP element.
   *
   * @param element Element to parse
   * @return Contained text
   */
    public static String getText(SOAPElement element) {
        String value = element.getValue();
        return (value == null) ? "" : value.trim();
    }
}
