package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKPow extends Token {

    public TKPow(int line, int pos, SourceFile sf) {
        super("pow", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKPow(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKPow(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKPow text.");
    }
}
