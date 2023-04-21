package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class Rule {

    private StringDescription id;

    private HeadLiteral headLiteral;

    private StringDescription ruleSeparator;

    private ArrayList body;

    private boolean hasId;

    private boolean hasRuleSeparator;

    public int offsetInInput = -1;

    public Rule(StringDescription id, HeadLiteral headLiteral, StringDescription ruleSeparator, ArrayList body, int endOffset) {
        this.id = id;
        this.headLiteral = headLiteral;
        this.ruleSeparator = ruleSeparator;
        this.body = body;
        if (id == null) hasId = false; else hasId = true;
        if (ruleSeparator == null) hasRuleSeparator = false; else hasRuleSeparator = true;
        offsetInInput = endOffset;
    }

    public ArrayList getBody() {
        return body;
    }

    public boolean hasId() {
        return hasId;
    }

    public boolean hasRuleSeparator() {
        return hasRuleSeparator;
    }

    public HeadLiteral getHeadLiteral() {
        return headLiteral;
    }

    public StringDescription getId() {
        return id;
    }

    public StringDescription getRuleSeparator() {
        return ruleSeparator;
    }

    public int getOffsetInInput() {
        return offsetInInput;
    }
}
