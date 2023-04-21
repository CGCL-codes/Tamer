package org.jdmp.core.script.jdmp.node;

import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class TLt extends Token {

    public TLt() {
        super.setText("<");
    }

    public TLt(int line, int pos) {
        super.setText("<");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TLt(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLt(this);
    }

    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLt text.");
    }
}
