package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class TLte extends Token {

    public TLte() {
        super.setText("<=");
    }

    public TLte(int line, int pos) {
        super.setText("<=");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLte(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLte(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLte text.");
    }
}
