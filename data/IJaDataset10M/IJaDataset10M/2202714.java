package com.modelmetrics.common.sforce.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.axis.message.MessageElement;
import com.modelmetrics.common.sforce.util.MessageElementBuilder;
import com.sforce.soap.partner.sobject.SObject;

public class SobjectBuilder {

    public SObject build(Sproxy target) throws Exception {
        SObject ret = new SObject();
        ret.setType(target.getType());
        ret.setId(target.getId());
        int nonNullSize = 0;
        for (Iterator iter = target.getValueKeys().iterator(); iter.hasNext(); ) {
            if (target.hasValue((String) iter.next())) {
                nonNullSize++;
            }
        }
        MessageElement[] elements = new MessageElement[nonNullSize];
        int count = 0;
        for (Iterator iter = target.getValueKeys().iterator(); iter.hasNext(); ) {
            String element = (String) iter.next();
            if (target.getValue(element) != null) {
                if (element.indexOf(":") > -1) {
                    String[] names = element.split(":");
                    Sproxy child = new SproxyBuilder().buildEmpty(names[1]);
                    child.setValue(names[2], target.getValue(element));
                    SObject childSobject = this.build(child);
                    elements[count] = MessageElementBuilder.getMessageElement(names[0], childSobject);
                } else {
                    elements[count] = MessageElementBuilder.getMessageElement(element, target.getValue(element));
                }
                count++;
            } else {
                target.getNullKeys().add(element);
            }
        }
        ret.set_any(elements);
        String[] nullKeys = {};
        nullKeys = target.getNullKeys().toArray(nullKeys);
        ret.setFieldsToNull(nullKeys);
        return ret;
    }

    public SObject[] build(Collection<Sproxy> targets) throws Exception {
        Collection<SObject> ret = new ArrayList<SObject>();
        for (Iterator iter = targets.iterator(); iter.hasNext(); ) {
            Sproxy element = (Sproxy) iter.next();
            ret.add(this.build(element));
        }
        SObject[] template = new SObject[0];
        SObject[] toReturn = ret.toArray(template);
        return toReturn;
    }
}
