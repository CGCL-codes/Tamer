package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKTranif0 extends Token {

    public TKTranif0(int line, int pos, SourceFile sf) {
        super("tranif0", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKTranif0(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKTranif0(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKTranif0 text.");
    }
}
