package org.yaoqiang.bpmn.model.elements.conversations;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;

/**
 * ConversationLink
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class ConversationLink extends BaseElement {

    public ConversationLink(ConversationLinks parent) {
        super(parent, "conversationLink");
    }

    protected void fillStructure() {
        XMLAttribute attrName = new XMLAttribute(this, "name");
        XMLAttribute attrSourceRef = new XMLAttribute(this, "sourceRef");
        XMLAttribute attrTargetRef = new XMLAttribute(this, "targetRef");
        super.fillStructure();
        add(attrName);
        add(attrSourceRef);
        add(attrTargetRef);
    }

    public ConversationLinks getParent() {
        return (ConversationLinks) parent;
    }

    public final String getName() {
        return get("name").toValue();
    }

    public final String getSourceRef() {
        return get("sourceRef").toValue();
    }

    public final String getTargetRef() {
        return get("targetRef").toValue();
    }

    public final void setName(String name) {
        set("name", name);
    }

    public final void setSourceRef(String sourceRef) {
        set("sourceRef", sourceRef);
    }

    public final void setTargetRef(String targetRef) {
        set("targetRef", targetRef);
    }
}
