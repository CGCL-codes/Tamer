package org.yaoqiang.bpmn.model.elements.events;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.core.common.FlowElements;

/**
 * StartEvent
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class StartEvent extends CatchEvent {

    public StartEvent(FlowElements parent) {
        super(parent, "startEvent");
    }

    protected void fillStructure() {
        XMLAttribute attrIsInterrupting = new XMLAttribute(this, "isInterrupting", Boolean.TRUE.toString());
        super.fillStructure();
        add(attrIsInterrupting);
    }

    public final boolean isInterrupting() {
        return Boolean.parseBoolean(get("isInterrupting").toValue());
    }
}
