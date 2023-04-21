package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TTPeriod extends Token {

    public TTPeriod(int line, int pos, SourceFile sf) {
        super(".", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TTPeriod(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTTPeriod(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TTPeriod text.");
    }
}
