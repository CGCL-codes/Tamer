package org.jdmp.core.script.jdmp.node;

import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class TLdiv extends Token {

    public TLdiv() {
        super.setText("\\");
    }

    public TLdiv(int line, int pos) {
        super.setText("\\");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TLdiv(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLdiv(this);
    }

    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLdiv text.");
    }
}
