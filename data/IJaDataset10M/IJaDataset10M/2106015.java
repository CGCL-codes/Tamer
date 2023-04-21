package org.jdmp.core.script.jdmp.node;

import org.jdmp.core.script.jdmp.analysis.*;

@SuppressWarnings("nls")
public final class TDotPower extends Token {

    public TDotPower() {
        super.setText(".^");
    }

    public TDotPower(int line, int pos) {
        super.setText(".^");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TDotPower(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTDotPower(this);
    }

    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TDotPower text.");
    }
}
