package websiteschema.cluster.analyzer.fields;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ray
 */
public class ExtractUtil {

    private static final ExtractUtil ins = new ExtractUtil();

    public static ExtractUtil getInstance() {
        return ins;
    }

    public String getNodeText(Node node) {
        String ret = "";
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                ret += child.getNodeValue();
            }
        }
        return ret;
    }

    public void extractNodeText(Node node, StringBuilder ret) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                ret.append(child.getNodeValue());
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                extractNodeText(child, ret);
            }
        }
    }
}
