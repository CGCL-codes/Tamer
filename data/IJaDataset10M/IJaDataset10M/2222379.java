package org.servingMathematics.qti.domImpl;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.imsglobal.qti.dom.*;

/**
 * TODO Description
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 *
 */
public class ResponseFlowControlImpl extends ElementImpl implements ResponseFlowControl {

    public ResponseFlowControlImpl(Element element) {
        super(element);
    }

    public Expression getExpression() {
        String childElementName;
        Element[] childElements = QTIDOMHelper.getChildElements(this);
        for (int i = 0; i < childElements.length; i++) {
            childElementName = childElements[i].getNodeName();
            if (!childElementName.equals("exitResponse") && !childElementName.equals("setOutcomeValue") && !childElementName.equals("responseCondition")) {
                return new ExpressionImpl(childElements[i]);
            }
        }
        return null;
    }

    public void setExpression(Expression expression) {
        String childElementName;
        Element[] childElements = QTIDOMHelper.getChildElements(this);
        for (int i = 0; i < childElements.length; i++) {
            childElementName = childElements[i].getNodeName();
            if (!childElementName.equals("exitResponse") && !childElementName.equals("setOutcomeValue") && !childElementName.equals("responseCondition")) {
                if (i == childElements.length - 1) {
                    removeChild(childElements[i]);
                    appendQTIChild((Element) expression);
                } else {
                    insertBeforeQTI((Element) expression, childElements[i]);
                    removeChild(childElements[i]);
                }
                return;
            }
        }
        appendQTIChild((Element) expression);
    }

    public ResponseRule[] getResponseRules() {
        String childElementName;
        Vector<ResponseRule> childElements = new Vector<ResponseRule>();
        Element childElement = QTIDOMHelper.getFirstChildElement(this);
        while (childElement != null) {
            childElementName = childElement.getNodeName();
            if (childElementName.equals("exitResponse")) {
                childElements.add(new ExitResponseImpl(childElement));
            } else if (childElementName.equals("setOutcomeValue")) {
                childElements.add(new SetOutcomeValueImpl(childElement));
            } else if (childElementName.equals("responseCondition")) {
                childElements.add(new ResponseConditionImpl(childElement));
            }
            childElement = QTIDOMHelper.getNextSiblingElement(childElement);
        }
        return childElements.toArray(new ResponseRule[childElements.size()]);
    }

    public SetOutcomeValue createOutcomeValue() {
        return new SetOutcomeValueImpl(createQTIElement("setOutcomeValue"));
    }

    public void setSetOutcomeValue(String identifier, SetOutcomeValue newOutcomeValue) {
        SetOutcomeValue setOutcomeValue;
        setOutcomeValue = getSetOutcomeValue(identifier);
        if (setOutcomeValue == null) {
            setOutcomeValue = new SetOutcomeValueImpl(appendQTIChild("setOutcomeValue"));
        }
        setOutcomeValue.setIdentifier(identifier);
        QTIDOMHelper.replaceContent(setOutcomeValue, newOutcomeValue);
    }

    public SetOutcomeValue getSetOutcomeValue(String identifier) {
        String id;
        NodeList nodeList = getElementsByTagName("setOutcomeValue");
        for (int i = 0; i < nodeList.getLength(); i++) {
            id = ((Element) nodeList.item(i)).getAttribute("identifier");
            if (id.equals(identifier)) {
                return new SetOutcomeValueImpl(((Element) nodeList.item(i)));
            }
        }
        return null;
    }

    public boolean isResponseIf() {
        if (getNodeName().equals("responseIf")) {
            return true;
        }
        return false;
    }

    public boolean isResponseElseIf() {
        if (getNodeName().equals("responseElseIf")) {
            return true;
        }
        return false;
    }

    public boolean isResponseElse() {
        if (getNodeName().equals("responseElse")) {
            return true;
        }
        return false;
    }
}
