package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKBool extends Token {

    public TKBool(int line, int pos, SourceFile sf) {
        super("bool", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKBool(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKBool(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKBool text.");
    }
}
