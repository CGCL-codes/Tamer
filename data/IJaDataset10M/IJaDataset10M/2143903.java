package net.sourceforge.schemaspy.util;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;

public class DOMUtil {

    public static void printDOM(Node node, LineWriter out) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer xformer;
        boolean indentSpecified = false;
        try {
            factory.setAttribute("indent-number", new Integer(3));
            indentSpecified = true;
        } catch (IllegalArgumentException factoryDoesntSupportIndentNumber) {
        }
        xformer = factory.newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        if (!indentSpecified) xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        xformer.transform(new DOMSource(node), new StreamResult(out));
    }

    /**
     * Append the specified key/value pair of attributes to the <code>Node</code>.
     * @param node Node
     * @param name String
     * @param value String
     */
    public static void appendAttribute(Node node, String name, String value) {
        if (value != null) {
            Node attribute = node.getOwnerDocument().createAttribute(name);
            attribute.setNodeValue(value);
            node.getAttributes().setNamedItem(attribute);
        }
    }
}
