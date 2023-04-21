package org.sablecc.sablecc.syntax3.node;

import org.sablecc.sablecc.syntax3.analysis.*;

@SuppressWarnings("nls")
public final class TNullKeyword extends Token {

    public TNullKeyword() {
        super.setText("Null");
    }

    public TNullKeyword(int line, int pos) {
        super.setText("Null");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone() {
        return new TNullKeyword(getLine(), getPos());
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseTNullKeyword(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text) {
        throw new RuntimeException("Cannot change TNullKeyword text.");
    }
}
