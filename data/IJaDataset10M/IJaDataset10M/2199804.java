package org.yaoqiang.bpmn.model.elements.choreographyactivities;

import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.collaboration.ParticipantAssociations;
import org.yaoqiang.bpmn.model.elements.core.common.CorrelationKeys;
import org.yaoqiang.bpmn.model.elements.core.common.FlowElements;

/**
 * CallChoreography
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class CallChoreography extends ChoreographyActivity {

    public CallChoreography(FlowElements parent) {
        super(parent, "callChoreography");
    }

    protected void fillStructure() {
        XMLAttribute attrCalledChoreographyRef = new XMLAttribute(this, "calledChoreographyRef");
        CorrelationKeys refCorrelationKeys = new CorrelationKeys(this);
        ParticipantAssociations refParticipantAssociations = new ParticipantAssociations(this);
        super.fillStructure();
        add(attrCalledChoreographyRef);
        add(refCorrelationKeys);
        add(refParticipantAssociations);
    }

    public final String getCalledChoreographyRef() {
        return get("calledChoreographyRef").toValue();
    }
}
