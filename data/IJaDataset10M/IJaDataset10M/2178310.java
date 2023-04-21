package org.zamia.verilog.node;

import org.zamia.verilog.analysis.*;
import org.zamia.SourceFile;

@SuppressWarnings("nls")
public final class TKNotif0 extends Token {

    public TKNotif0(int line, int pos, SourceFile sf) {
        super("notif0", line, pos, sf);
    }

    @Override
    public Object clone() {
        return new TKNotif0(getLine(), getPos(), getSource());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTKNotif0(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TKNotif0 text.");
    }
}
