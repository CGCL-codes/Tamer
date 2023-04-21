package org.sablecc.objectmacro.syntax3.node;

import org.sablecc.objectmacro.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TLPar extends Token {

    public TLPar() {
        super.setText("(");
    }

    public TLPar(int line, int pos) {
        super.setText("(");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TLPar(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLPar(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TLPar text.");
    }
}
