package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TLvar extends Token {

    public TLvar() {
        super.setText("lvar");
    }

    public TLvar(int line, int pos) {
        super.setText("lvar");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLvar(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLvar(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLvar text.");
    }
}
