package org.sablecc.sablecc.node;

import org.sablecc.sablecc.analysis.*;
import org.sablecc.sablecc.node.Switch;
import org.sablecc.sablecc.node.TLArrow;
import org.sablecc.sablecc.node.Token;

public final class TLArrow extends Token {

    public TLArrow() {
        super.setText("<-");
    }

    public TLArrow(int line, int pos) {
        super.setText("<-");
        setLine(line);
        setPos(pos);
    }

    public Object clone() {
        return new TLArrow(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTLArrow(this);
    }

    public void setText(String text) {
        throw new RuntimeException("Cannot change TLArrow text.");
    }
}
