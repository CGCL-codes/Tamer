package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKXnor extends Token {

    public TKXnor(int line, int pos, SourceFile sf) {
        super("xnor", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKXnor(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKXnor(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKXnor text.");
    }
}
