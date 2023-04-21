package net.sourceforge.javabits.xml.xpath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sourceforge.javabits.lang.Strings;
import net.sourceforge.javabits.xml.dom.Elements;
import net.sourceforge.javabits.xml.dom.Nodes;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jochen Kuhnle
 */
public class XPathEvaluator {

    private XPathFactory factory = XPathFactory.newInstance();

    public static final Object quote(Object value) {
        Object result = null;
        if (value != null) {
            if (value instanceof Number) {
                result = value;
            } else {
                result = value;
            }
        }
        return result;
    }

    public static final Object[] convertParams(Object[] params) {
        Object[] result = new Object[params.length];
        for (int i = 0; i < params.length; ++i) {
            result[i] = quote(params[i]);
        }
        return result;
    }

    public Node node(Node context, XPathExpression xpath) {
        Node result = (Node) evaluate(context, XPathConstants.NODE, xpath);
        return result;
    }

    public Node node(Node context, String expression, Object... params) {
        Node result = (Node) evaluate(context, XPathConstants.NODE, expression, params);
        return result;
    }

    public List<Node> nodeList(Node context, String expression, Object... params) {
        XPathExpression e = xpath(expression, params);
        List<Node> nodeList = addNodes(new ArrayList<Node>(), context, e);
        return nodeList;
    }

    public List<Node> nodeList(Node context, XPathExpression expression) {
        List<Node> nodeList = addNodes(new ArrayList<Node>(), context, expression);
        return nodeList;
    }

    public <C extends Collection<? super Node>> C addNodes(C collection, Node context, XPathExpression xpath) {
        NodeList list = (NodeList) evaluate(context, XPathConstants.NODESET, xpath);
        for (int i = 0; i < list.getLength(); ++i) {
            collection.add(list.item(i));
        }
        return collection;
    }

    public List<String> textList(Node context, String expression, Object... params) {
        XPathExpression e = xpath(expression, params);
        List<String> nodeList = addTexts(new ArrayList<String>(), context, e);
        return nodeList;
    }

    public List<String> textList(Node context, XPathExpression expression) {
        List<String> nodeList = addTexts(new ArrayList<String>(), context, expression);
        return nodeList;
    }

    public <C extends Collection<? super String>> C addTexts(C collection, Node context, XPathExpression xpath) {
        NodeList list = (NodeList) evaluate(context, XPathConstants.NODESET, xpath);
        for (int i = 0; i < list.getLength(); ++i) {
            collection.add(list.item(i).getTextContent());
        }
        return collection;
    }

    public Node[] nodes(Node context, String expression, Object... params) {
        Node[] nodes = Nodes.EMPTY_ARRAY;
        NodeList list = (NodeList) evaluate(context, XPathConstants.NODESET, expression, params);
        if (list.getLength() > 0) {
            nodes = new Node[list.getLength()];
            for (int i = 0; i < list.getLength(); ++i) {
                nodes[i] = list.item(i);
            }
        }
        return nodes;
    }

    public Element[] elements(Node context, String expression, Object... params) {
        Element[] elements = Elements.EMPTY_ARRAY;
        NodeList list = (NodeList) evaluate(context, XPathConstants.NODESET, expression, params);
        if (list.getLength() > 0) {
            elements = new Element[list.getLength()];
            int k = 0;
            for (int i = 0; i < list.getLength(); ++i) {
                Node child = list.item(i);
                if (child instanceof Element) {
                    elements[k] = (Element) child;
                    ++k;
                }
            }
            if (k < elements.length) {
                Element[] temp = new Element[k];
                System.arraycopy(elements, 0, temp, 0, k);
                elements = temp;
            }
        }
        return elements;
    }

    public final String text(Node context, String expression, Object... params) {
        XPathExpression xpath = xpath(expression, params);
        String result = text(context, xpath);
        return result;
    }

    public final String text(Node context, XPathExpression xpath) {
        Node node = (Node) evaluate(context, XPathConstants.NODE, xpath);
        String result = null;
        if (node != null) {
            result = node.getTextContent();
        }
        return result;
    }

    public String[] texts(Node context, String expression, Object... params) {
        XPathExpression xpath = xpath(expression, params);
        String[] texts = texts(context, xpath);
        return texts;
    }

    public String[] texts(Node context, XPathExpression xpath) {
        String[] texts = Strings.EMPTY_ARRAY;
        NodeList list = (NodeList) evaluate(context, XPathConstants.NODESET, xpath);
        if (list.getLength() > 0) {
            texts = new String[list.getLength()];
            for (int i = 0; i < list.getLength(); ++i) {
                texts[i] = list.item(i).getTextContent();
            }
        }
        return texts;
    }

    public XPathExpression xpath(String expression, Object... params) {
        try {
            String e = String.format(expression, convertParams(params));
            XPath xpath = factory.newXPath();
            XPathExpression xpathExpression = xpath.compile(e);
            return xpathExpression;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public Object evaluate(Node context, QName type, String expression, Object... params) {
        XPathExpression xpath = xpath(expression, params);
        return evaluate(context, type, xpath);
    }

    public Object evaluate(Node context, QName type, XPathExpression xpath) {
        try {
            Object result = xpath.evaluate(context, type);
            return result;
        } catch (XPathExpressionException e) {
            throw new XPathEvaluationException(e);
        }
    }
}
