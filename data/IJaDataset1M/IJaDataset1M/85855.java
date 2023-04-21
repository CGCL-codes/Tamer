package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class TGteq extends Token {

    public TGteq() {
        super.setText(">=");
    }

    public TGteq(int line, int pos) {
        super.setText(">=");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TGteq(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTGteq(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TGteq text.");
    }
}
