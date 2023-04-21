package org.yaoqiang.bpmn.model.elements.core.common;

import java.util.ArrayList;
import java.util.List;
import org.yaoqiang.bpmn.model.BPMNModelUtils;
import org.yaoqiang.bpmn.model.elements.XMLAttribute;
import org.yaoqiang.bpmn.model.elements.XMLElement;
import org.yaoqiang.bpmn.model.elements.XMLTextElements;
import org.yaoqiang.bpmn.model.elements.collaboration.Participant;
import org.yaoqiang.bpmn.model.elements.core.foundation.BaseElement;
import org.yaoqiang.bpmn.model.elements.core.foundation.RootElement;
import org.yaoqiang.bpmn.model.elements.core.foundation.RootElements;

/**
 * PartnerEntity
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class PartnerEntity extends BaseElement implements RootElement {

    public PartnerEntity(RootElements parent) {
        super(parent, "partnerEntity");
    }

    protected void fillStructure() {
        XMLAttribute attrName = new XMLAttribute(this, "name");
        XMLTextElements refParticipantRef = new XMLTextElements(this, "participantRef");
        super.fillStructure();
        add(attrName);
        add(refParticipantRef);
    }

    public final XMLTextElements getParticipantRefs() {
        return (XMLTextElements) get("participantRef");
    }

    public final List<XMLElement> getParticipantRefList() {
        return getParticipantRefs().getXMLElements();
    }

    public final List<XMLElement> getRefParticipantList() {
        List<XMLElement> els = new ArrayList<XMLElement>();
        for (XMLElement partRef : getParticipantRefList()) {
            Participant part = BPMNModelUtils.getDefinitions(parent).getParticipant(partRef.toValue());
            if (part != null) {
                els.add(part);
            }
        }
        return els;
    }
}
