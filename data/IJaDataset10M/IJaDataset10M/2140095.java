package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TTTrigger extends Token {

    public TTTrigger(int line, int pos, SourceFile sf) {
        super("->", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TTTrigger(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTTrigger(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TTTrigger text.");
    }
}
