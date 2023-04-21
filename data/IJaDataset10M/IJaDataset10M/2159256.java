package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TTAt extends Token {

    public TTAt(int line, int pos, SourceFile sf) {
        super("@", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TTAt(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTAt(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TTAt text.");
    }
}
