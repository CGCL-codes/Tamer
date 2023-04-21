package com.siemens.ct.exi.grammar.rule;

import java.util.List;
import com.siemens.ct.exi.Constants;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.datatype.BuiltIn;
import com.siemens.ct.exi.grammar.event.Attribute;
import com.siemens.ct.exi.grammar.event.Characters;
import com.siemens.ct.exi.grammar.event.EndElement;
import com.siemens.ct.exi.grammar.event.EventType;
import com.siemens.ct.exi.grammar.event.StartElement;

public class SchemaLessRuleStartTag extends SchemaLessRuleContent {

    protected SchemaLessRuleElement elementContent;

    public SchemaLessRuleStartTag() {
        super();
        elementContent = new SchemaLessRuleElement();
    }

    public int get2ndLevelEventCode(EventType eventType, FidelityOptions fidelityOptions) {
        List<EventType> startTagContent = get2ndLevelEventsStartTagItems(fidelityOptions);
        int ec = getEventCode(eventType, startTagContent);
        if (ec == Constants.NOT_FOUND) {
            ec = getEventCode(eventType, get2ndLevelEventsChildContentItems(fidelityOptions));
            if (ec != Constants.NOT_FOUND) {
                ec += startTagContent.size();
            }
        }
        return ec;
    }

    public EventType get2ndLevelEvent(int eventCode, FidelityOptions fidelityOptions) {
        EventType eventType = null;
        List<EventType> startTagContent = get2ndLevelEventsStartTagItems(fidelityOptions);
        if (eventCode < startTagContent.size()) {
            eventType = startTagContent.get(eventCode);
        } else {
            eventType = get2ndLevelEventsChildContentItems(fidelityOptions).get(eventCode - startTagContent.size());
        }
        return eventType;
    }

    public int get2ndLevelCharacteristics(FidelityOptions fidelityOptions) {
        int ch2 = get2ndLevelEventsStartTagItems(fidelityOptions).size();
        ch2 += get2ndLevelEventsChildContentItems(fidelityOptions).size();
        if (get3rdLevelCharacteristics(fidelityOptions) > 0) {
            ch2++;
        }
        return ch2;
    }

    @Override
    public Rule getElementContentRule() {
        return elementContent;
    }

    @Override
    public Rule getElementContentRuleForUndeclaredSE() {
        return getElementContentRule();
    }

    @Override
    public void learnStartElement(String uri, String localName) {
        addRule(new StartElement(uri, localName), getElementContentRule());
    }

    @Override
    public void learnEndElement() {
        addTerminalRule(new EndElement());
    }

    @Override
    public void learnAttribute(String uri, String localName) {
        addRule(new Attribute(uri, localName), this);
    }

    @Override
    public void learnCharacters() {
        addRule(new Characters(BuiltIn.DEFAULT_VALUE_NAME, BuiltIn.DEFAULT_DATATYPE), getElementContentRule());
    }
}
