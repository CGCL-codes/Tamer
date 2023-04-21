package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKAlwaysFf extends Token {

    public TKAlwaysFf(int line, int pos, SourceFile sf) {
        super("always_ff", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKAlwaysFf(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKAlwaysFf(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKAlwaysFf text.");
    }
}
