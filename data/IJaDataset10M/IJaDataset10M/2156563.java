package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKAsinh extends Token {

    public TKAsinh(int line, int pos, SourceFile sf) {
        super("asinh", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKAsinh(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKAsinh(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKAsinh text.");
    }
}
