package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TTDiv extends Token {

    public TTDiv(int line, int pos, SourceFile sf) {
        super("/", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TTDiv(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTDiv(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TTDiv text.");
    }
}
