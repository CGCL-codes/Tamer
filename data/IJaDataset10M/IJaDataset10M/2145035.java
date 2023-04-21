package oext.model.rules;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import oext.model.NodeModel;

public class OneAttributeEquality implements EqualityRule {

    String attributeName = "";

    public OneAttributeEquality() {
        super();
    }

    public OneAttributeEquality(String attributeName) {
        this.attributeName = attributeName;
    }

    public float equality(NodeModel one, NodeModel two) {
        float ret = 1.0f;
        NamedNodeMap map1 = one.getAttributes();
        NamedNodeMap map2 = two.getAttributes();
        Node node1 = map1.getNamedItem(attributeName);
        Node node2 = map2.getNamedItem(attributeName);
        if ((node1 == null) || (node2 == null)) {
            ret = 0;
        } else {
            String value1 = node1.getNodeValue();
            String value2 = node2.getNodeValue();
            if ((value1 == null) || (value2 == null)) {
                ret = 0;
            } else if (value1.equals(value2)) {
            } else {
                ret = 0;
            }
        }
        return ret;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
   * Visitor call function following the vistor pattern
   * 
   * @param visitor vistor visiting the rule
   */
    public void visit(EqualityRuleVisitor visitor) {
        visitor.visitOneAttributeEquality(this);
    }
}
