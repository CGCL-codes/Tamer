package com.siemens.ct.exi.grammar.rule;

import com.siemens.ct.exi.Constants;
import com.siemens.ct.exi.FidelityOptions;
import com.siemens.ct.exi.grammar.event.EventType;

public class SchemaLessDocContent extends AbstractSchemaLessRule {

    private static final long serialVersionUID = 3223520398225946713L;

    protected Rule docEnd;

    public SchemaLessDocContent(Rule docEnd) {
        super();
        this.docEnd = docEnd;
        this.addRule(START_ELEMENT_GENERIC, docEnd);
    }

    public SchemaLessDocContent(Rule docEnd, String label) {
        this(docEnd);
        this.setLabel(label);
    }

    @Override
    public boolean hasSecondOrThirdLevel(FidelityOptions fidelityOptions) {
        return (fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_DTD) || fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_COMMENT) || fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_PI));
    }

    public int get2ndLevelEventCode(EventType eventType, FidelityOptions fidelityOptions) {
        if (eventType == EventType.DOC_TYPE && fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_DTD)) {
            return 0;
        }
        return Constants.NOT_FOUND;
    }

    public EventType get2ndLevelEvent(int eventCode, FidelityOptions fidelityOptions) {
        if (eventCode == 0 && fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_DTD)) {
            return EventType.DOC_TYPE;
        }
        return null;
    }

    public int get2ndLevelCharacteristics(FidelityOptions fidelityOptions) {
        int ch2 = get3rdLevelCharacteristics(fidelityOptions) > 0 ? 1 : 0;
        ch2 += fidelityOptions.isFidelityEnabled(FidelityOptions.FEATURE_DTD) ? 1 : 0;
        return ch2;
    }
}
