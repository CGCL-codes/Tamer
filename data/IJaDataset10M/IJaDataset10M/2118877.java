package issrg.utils.handler;

import org.w3c.dom.*;
import org.apache.log4j.*;

/**
 *
 * @author ls97
 */
public class EncodeXML {

    private static Logger logger = Logger.getLogger(EncodeXML.class);

    /** Creates a new instance of EncodeXML */
    public EncodeXML() {
    }

    public String encode(Node ele, int indent) {
        String prefix = new String();
        for (int i = 0; i < indent; i++) prefix += " ";
        String buf = new String();
        if (!Text.class.isAssignableFrom(ele.getClass())) {
            buf += prefix + "<" + ele.getNodeName();
            NamedNodeMap atts = ele.getAttributes();
            for (int i = 0; i < atts.getLength(); i++) {
                Node node = atts.item(i);
                buf += " " + node.getNodeName() + "=" + '"' + node.getNodeValue() + '"';
            }
            if (ele.getChildNodes().getLength() > 0) buf += ">\r\n"; else buf += "/>\r\n";
        } else buf += prefix + ele.getNodeValue() + "\r\n";
        NodeList children = ele.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            buf += this.encode(node, indent + 4);
        }
        if (children.getLength() > 0) buf += prefix + "</" + ele.getNodeName() + ">\r\n";
        return buf;
    }
}
