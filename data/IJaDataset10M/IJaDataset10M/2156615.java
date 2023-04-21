package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKSpecify extends Token {

    public TKSpecify(int line, int pos, SourceFile sf) {
        super("specify", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKSpecify(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKSpecify(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKSpecify text.");
    }
}
