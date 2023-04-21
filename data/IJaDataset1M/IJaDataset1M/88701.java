package org.yaoqiang.bpmn.model.elements.core.common;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;

/**
 * CorrelationPropertyRetrievalExpression
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class CorrelationPropertyRetrievalExpression extends BaseElement {

    public CorrelationPropertyRetrievalExpression(CorrelationPropertyRetrievalExpressions parent) {
        super(parent, "correlationPropertyRetrievalExpression");
    }

    protected void fillStructure() {
        XMLAttribute attrMessageRef = new XMLAttribute(this, "messageRef");
        FormalExpression refMessagePath = new FormalExpression(this, "messagePath");
        super.fillStructure();
        add(attrMessageRef);
        add(refMessagePath);
    }

    public CorrelationPropertyRetrievalExpressions getParent() {
        return (CorrelationPropertyRetrievalExpressions) parent;
    }
}
