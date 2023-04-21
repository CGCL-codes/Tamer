package mySBML;

import java.util.ArrayList;
import mySBML.utilities.DomUtilities;
import mySBML.utilities.XmlProblem;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AlgebraicRule extends Rule {

    @Override
    public String tag() {
        return "algebraicRule";
    }

    public String[] getAllowedChildren() {
        String[] allowed = new String[] { "notes", "annotation", "math" };
        return allowed;
    }

    public String[] getAllowedAttributes() {
        String[] allowed = new String[] { "metaid", "sboterm" };
        return allowed;
    }

    @Override
    public void copy(AbstractSbml original) {
        if (original.getClass() != this.getClass()) return;
        AlgebraicRule rule = (AlgebraicRule) original;
        math.copy(rule.math);
    }

    @Override
    public AlgebraicRule copy() {
        AlgebraicRule copy = new AlgebraicRule();
        copy.copy(this);
        return copy;
    }

    @Override
    public ArrayList<XmlProblem> loadXML(Node node) {
        ArrayList<XmlProblem> problems = super.loadXML(node);
        DomUtilities.removeEmptyTextChildren(node);
        int mathNodes = 0;
        for (int i = 0; i < node.getChildNodes().getLength(); i++) if (node.getChildNodes().item(i).getNodeName().equalsIgnoreCase("math")) {
            if (mathNodes == 0) problems.addAll(math.loadXML(node.getChildNodes().item(i)));
            mathNodes++;
        }
        if (mathNodes > 1) problems.add(XmlProblem.redundantChildElements(tag(), "math"));
        return problems;
    }

    @Override
    public Element toSBML(Node parentNode) {
        Element node = super.toSBML(parentNode);
        node.appendChild(math.toSBML(node));
        return node;
    }

    @Override
    public String getXPath() {
        String xpath = new String("model");
        int counter = 0;
        for (int i = 0; i < getModel().getRules().size(); i++) if (getModel().getRules().get(i).getClass() == this.getClass()) {
            counter++;
            if (getModel().getRules().get(i) == this) break;
        }
        xpath += " // algebraicRule[" + counter + "]";
        return xpath;
    }
}
