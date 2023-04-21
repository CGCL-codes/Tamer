package org.jdmp.core.script.jdmp.node;

import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class TOr extends Token {

    public TOr() {
        super.setText("|");
    }

    public TOr(int line, int pos) {
        super.setText("|");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TOr(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTOr(this);
    }

    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TOr text.");
    }
}
