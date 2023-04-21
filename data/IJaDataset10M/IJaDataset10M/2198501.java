package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKPulldown extends Token {

    public TKPulldown(int line, int pos, SourceFile sf) {
        super("pulldown", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKPulldown(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKPulldown(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKPulldown text.");
    }
}
