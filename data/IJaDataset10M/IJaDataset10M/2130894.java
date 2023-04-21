package org.sablecc.java.node;

import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class TShiftLeft extends Token {

    public TShiftLeft() {
        super.setText("<<");
    }

    public TShiftLeft(int line, int pos) {
        super.setText("<<");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TShiftLeft(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTShiftLeft(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TShiftLeft text.");
    }
}
