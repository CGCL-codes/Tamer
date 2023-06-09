package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TSet extends Token {

    public TSet() {
        super.setText("set");
    }

    public TSet(int line, int pos) {
        super.setText("set");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TSet(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTSet(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TSet text.");
    }
}
