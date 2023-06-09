package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TGte extends Token {

    public TGte() {
        super.setText(">=");
    }

    public TGte(int line, int pos) {
        super.setText(">=");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TGte(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTGte(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TGte text.");
    }
}
