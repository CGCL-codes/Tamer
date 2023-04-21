package org.jdmp.core.script.jdmp.node;

import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class TLogicalOr extends Token {

    public TLogicalOr() {
        super.setText("||");
    }

    public TLogicalOr(int line, int pos) {
        super.setText("||");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TLogicalOr(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLogicalOr(this);
    }

    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLogicalOr text.");
    }
}
