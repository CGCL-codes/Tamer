package it.dangelo.javabinding.test.utility;

import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPathException;
import org.dom4j.tree.DefaultText;
import org.jaxen.expr.Step;
import org.saxpath.SAXPathException;

public class XPathUtility {

    private Node node;

    public XPathUtility(Node node) {
        super();
        this.node = node;
    }

    public void setValue(String xpath, String value) {
        this.setValue(xpath, new DefaultText(value));
    }

    public void setValue(String xpath, Node value) {
        XPathUtility.setValue(this.node, xpath, value);
    }

    public static void setValue(Node node, String xpath, Node value) {
        try {
            XPathCompiler compiler = XPathCompiler.getInstance(xpath);
            List<Step> steps = compiler.getSteps();
            int i = steps.size();
            for (Step step : steps) {
                --i;
                int type = 0;
                String text = step.getText();
                if (text.startsWith("child::")) {
                    type = 1;
                    text = text.substring("child::".length());
                }
                if (text.startsWith("attribute::")) {
                    type = 2;
                    text = text.substring("attribute::".length());
                }
                Node el = node;
                if (!text.endsWith("()") && node != null) el = node.selectSingleNode(text);
                if (el == null) {
                    if (type == 1) {
                        el = DocumentHelper.createElement(text);
                        if (node instanceof Element) {
                            Element element2 = (Element) node;
                            element2.add(el);
                        }
                        if (node instanceof Document) {
                            Document document = (Document) node;
                            document.add(el);
                        }
                        node = el;
                    }
                    if (type == 2) {
                        if (node instanceof Element) {
                            Element element2 = (Element) node;
                            Attribute attr = DocumentHelper.createAttribute(element2, new QName(text), value.getStringValue());
                            element2.add(attr);
                            break;
                        }
                    }
                } else node = el;
                if (i == 0) {
                    if (node instanceof Element) {
                        Element element2 = (Element) node;
                        element2.add(value);
                    }
                }
            }
        } catch (SAXPathException e) {
            throw new XPathException("Error to set the xpath value", e);
        }
    }

    public static Node getValue(Node node, String xpath) {
        return node.selectSingleNode(xpath);
    }

    public Node getValue(String xpath) {
        return XPathUtility.getValue(this.node, xpath);
    }

    public Node getNode() {
        return this.node;
    }
}
