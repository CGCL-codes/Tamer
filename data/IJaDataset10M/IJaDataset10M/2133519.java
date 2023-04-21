package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKSnochange extends Token {

    public TKSnochange(int line, int pos, SourceFile sf) {
        super("$nochange", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKSnochange(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKSnochange(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKSnochange text.");
    }
}
