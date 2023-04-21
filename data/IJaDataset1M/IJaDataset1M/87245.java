package watij.xpath;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import watij.finders.BaseFindable;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: Apr 14, 2006
 * Time: 4:16:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class XPath extends BaseFindable {

    String expression;

    public XPath(String expression) {
        this.expression = expression;
    }

    public List<Element> find(Element element) throws Exception {
        List<Element> list = new ArrayList<Element>();
        javax.xml.xpath.XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xpath.evaluate(expression, element, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                list.add((Element) node);
            }
        }
        return list;
    }
}
