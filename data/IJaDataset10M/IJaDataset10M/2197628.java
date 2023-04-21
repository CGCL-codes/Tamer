package org.omwg.mediation.parser.hrsyntax.node;

import org.omwg.mediation.parser.hrsyntax.analysis.*;

public final class TLpar extends Token {

    public TLpar() {
        super.setText("(");
    }

    public TLpar(int line, int pos) {
        super.setText("(");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TLpar(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLpar(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TLpar text.");
    }
}
